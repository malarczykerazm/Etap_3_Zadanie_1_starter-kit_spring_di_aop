package pl.spring.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.runner.RunWith;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.exception.BookAlreadyExistsException;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.exception.WrongInputException;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookDaoAdvisor implements MethodBeforeAdvice {

	@Autowired
	private BookDao bookDao;

	@Override
	public void before(Method consideredMethod, Object[] paramsOfConsideredMethod, Object instanceOfConsideredClass)
			throws Throwable {

		if (hasAnnotation(consideredMethod, instanceOfConsideredClass, NullableId.class)) {
			checkNotNullId(paramsOfConsideredMethod[0]);
		}
	}

	private void checkNotNullId(Object parameterOfConsideredMethod)
			throws BookAlreadyExistsException, WrongInputException, BookNotNullIdException {
		if (!(parameterOfConsideredMethod.getClass().equals(BookTo.class))) {
			throw new WrongInputException("The input for the considered method is incorrect.");
		}
		if (null != ((BookTo) parameterOfConsideredMethod).getId()) {
			throw new BookNotNullIdException();
		}
		if (bookDao.findBookByTitle(((BookTo) parameterOfConsideredMethod).getTitle()).size() >= 1) {
			throw new BookAlreadyExistsException("The considered book already exists in the database.");
		}
	}

	private boolean hasAnnotation(Method consideredMethod, Object instanceOfConsideredClass,
			Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
		boolean doesHaveAnnotation = (consideredMethod.getAnnotation(annotationClass) != null);

		if (!doesHaveAnnotation && instanceOfConsideredClass != null) {
			doesHaveAnnotation = (instanceOfConsideredClass.getClass()
					.getMethod(consideredMethod.getName(), consideredMethod.getParameterTypes())
					.getAnnotation(annotationClass) != null);
		}
		return doesHaveAnnotation;
	}
}
