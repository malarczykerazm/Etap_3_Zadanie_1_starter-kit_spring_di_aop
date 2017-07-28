package pl.spring.demo.aop;


import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.MethodBeforeAdvice;

import pl.spring.demo.annotation.NeedsNewBookId;
import pl.spring.demo.common.Sequence;
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.to.IdAware;

public class BookIdAdvisor implements MethodBeforeAdvice {

    private Sequence sequence;

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

        if (hasAnnotation(method, o, NeedsNewBookId.class)) {
            createNewBookId(objects[0], o);
        }
    }

    private void createNewBookId(Object arg, Object o) {
    	if (o instanceof IdAware && ((IdAware) o).getId() != null) {
    		BookDao bookDao = (BookDao) o;
    		List<BookTo> list = bookDao.findAll();
    		BookTo book = (BookTo) arg;
    		book.setId(sequence.nextValue(list));
    	}
    }

    private boolean hasAnnotation (Method method, Object o, Class annotationClazz) throws NoSuchMethodException {
        boolean hasAnnotation = method.getAnnotation(annotationClazz) != null;

        if (!hasAnnotation && o != null) {
            hasAnnotation = o.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(annotationClazz) != null;
        }
        return hasAnnotation;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
    
}
