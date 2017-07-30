package pl.spring.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.exception.BookAlreadyExistsException;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.exception.WrongInputException;
import pl.spring.demo.to.BookTo;

public class BookDaoAdvisor implements MethodBeforeAdvice {

	@Override
	public void before(Method consideredMethod, Object[] paramsOfConsideredMethod, Object instanceOfConsideredClass)
			throws Throwable {

		if (hasAnnotation(consideredMethod, instanceOfConsideredClass, NullableId.class)) {
			checkNotNullId(paramsOfConsideredMethod[0], instanceOfConsideredClass);
		}
	}

	private void checkNotNullId(Object parameterOfConsideredMethod, Object instanceOfConsideredClass) {
		if (!(parameterOfConsideredMethod.getClass().equals(BookTo.class))) {
			throw new WrongInputException("The input for the considered method is incorrect.");
		}
		if (null != ((BookTo) parameterOfConsideredMethod).getId()) {
			throw new BookNotNullIdException();
		}
		if (!(instanceOfConsideredClass instanceof BookDao)) {
			throw new WrongInputException("The advisor was used with a class it was not supposed to.");
		}
		if (((BookDao) instanceOfConsideredClass).findBookByTitle(((BookTo) parameterOfConsideredMethod).getTitle())
				.size() >= 1) {
			throw new BookAlreadyExistsException("The considered book already exists in the database.");
		}
	}

	private boolean hasAnnotation(Method consideredMethod, Object instanceOfConsideredClass,
			Class<? extends Annotation> annotationClazz) throws NoSuchMethodException {
		boolean doesHaveAnnotation = (consideredMethod.getAnnotation(annotationClazz) != null);

		if (!doesHaveAnnotation && instanceOfConsideredClass != null) {
			doesHaveAnnotation = (instanceOfConsideredClass.getClass()
					.getMethod(consideredMethod.getName(), consideredMethod.getParameterTypes())
					.getAnnotation(annotationClazz) != null);
		}
		return doesHaveAnnotation;
	}
}
