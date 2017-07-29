package pl.spring.demo.aop;


import org.springframework.aop.MethodBeforeAdvice;
import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.IdAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BookDaoAdvisor implements MethodBeforeAdvice {

    @Override
    public void before(Method consideredMethod, Object[] paramsOfConsideredMethod, Object instanceOfConsideredClass) throws Throwable {

        if (hasAnnotation(consideredMethod, instanceOfConsideredClass, NullableId.class)) {
            checkNotNullId(paramsOfConsideredMethod[0]);
        }
    }

    private void checkNotNullId(Object instanceOfConsideredClass) {
        if (instanceOfConsideredClass instanceof IdAware && ((IdAware) instanceOfConsideredClass).getId() != null) {
            throw new BookNotNullIdException();
        }
    }

    private boolean hasAnnotation (Method consideredMethod, Object instanceOfConsideredClass, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        boolean doesHaveAnnotation = (consideredMethod.getAnnotation(annotationClass) != null);

        if (!doesHaveAnnotation && instanceOfConsideredClass != null) {
            doesHaveAnnotation = (instanceOfConsideredClass.getClass().getMethod(consideredMethod.getName(), consideredMethod.getParameterTypes()).getAnnotation(annotationClass) != null);
        }
        return doesHaveAnnotation;
    }
}
