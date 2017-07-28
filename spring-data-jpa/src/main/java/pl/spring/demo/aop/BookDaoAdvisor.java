package pl.spring.demo.aop;


import org.springframework.aop.MethodBeforeAdvice;
import pl.spring.demo.annotation.NullableId;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.IdAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BookDaoAdvisor implements MethodBeforeAdvice {

    @Override
    public void before(Method currentMethod, Object[] argsOfCurrentMethod, Object instanceOfCurrentClass) throws Throwable {

        if (hasAnnotation(currentMethod, instanceOfCurrentClass, NullableId.class)) {
            checkNotNullId(argsOfCurrentMethod[0]);
        }
    }

    private void checkNotNullId(Object instanceOfCurrentClass) {
        if (instanceOfCurrentClass instanceof IdAware && ((IdAware) instanceOfCurrentClass).getId() != null) {
            throw new BookNotNullIdException();
        }
    }

    private boolean hasAnnotation (Method currentMethod, Object instanceOfCurrentClass, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        boolean doesHaveAnnotation = (currentMethod.getAnnotation(annotationClass) != null);

        if (!doesHaveAnnotation && instanceOfCurrentClass != null) {
            doesHaveAnnotation = (instanceOfCurrentClass.getClass().getMethod(currentMethod.getName(), currentMethod.getParameterTypes()).getAnnotation(annotationClass) != null);
        }
        return doesHaveAnnotation;
    }
}
