package pl.spring.demo.aop;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.dao.BookDao;
import pl.spring.demo.dao.impl.BookDaoImpl;
import pl.spring.demo.to.BookTo;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "CommonServiceTest-context.xml")
public class AopTest {
	
	//@Autowired
	private BookDao bookDao = new BookDaoImpl();

	@Test
	public void shouldSaveNewBookWithNewId() {
		BookTo bookTo = new BookTo();
		//bookTo.setTitle("Test book");
		//bookTo.setId(7L);
		bookDao.save(bookTo);
		
		Assert.assertTrue(bookTo.getId() == 7L);
	}

}
