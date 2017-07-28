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
    public void before(Method currentMethod, Object[] argsOfCurrentMethod, Object instanceOfCurrentClass) throws Throwable {

        if (hasAnnotation(currentMethod, instanceOfCurrentClass, MayNeedNewBookId.class)) {
            createNewBookId(argsOfCurrentMethod[0], instanceOfCurrentClass);
        }
    }

    private void createNewBookId(Object parameterOfCurrentMethod, Object instanceOfCurrentClass) {
    	BookDao bookDao = (BookDao) instanceOfCurrentClass;
    	List<BookTo> list = bookDao.findAll();
    	BookTo book = (BookTo) parameterOfCurrentMethod;
    	book.setId(sequence.nextValue(list));
    }

    private boolean hasAnnotation (Method currentdMethod, Object instanceOfCurrentClass, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        boolean doesHaveAnnotation = (currentdMethod.getAnnotation(annotationClass) != null);

        if (!doesHaveAnnotation && instanceOfCurrentClass != null) {
        	doesHaveAnnotation = (instanceOfCurrentClass.getClass().getMethod(currentdMethod.getName(), currentdMethod.getParameterTypes()).getAnnotation(annotationClass) != null);
        }
        return doesHaveAnnotation;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
    
}
