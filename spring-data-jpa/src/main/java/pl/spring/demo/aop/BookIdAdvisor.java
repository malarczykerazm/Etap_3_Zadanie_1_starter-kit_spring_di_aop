package pl.spring.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.MethodBeforeAdvice;

import pl.spring.demo.annotation.MayNeedNewBookId;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.to.BookTo;

public class BookIdAdvisor implements MethodBeforeAdvice {

	private Sequence sequence;

	@Override
	public void before(Method cosnideredMethod, Object[] paramsOfConsideredMethod, Object instanceOfConsideredClass)
			throws Throwable {

		if (hasAnnotation(cosnideredMethod, instanceOfConsideredClass, MayNeedNewBookId.class)) {
			createNewBookId(paramsOfConsideredMethod[0], instanceOfConsideredClass);
		}
	}

	private void createNewBookId(Object parameterOfConsideredMethod, Object instanceOfConsideredClass) {
			List<BookTo> list = ((BookDao) instanceOfConsideredClass).findAll();
			((BookTo) parameterOfConsideredMethod).setId(sequence.nextValue(list));	
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

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

}
