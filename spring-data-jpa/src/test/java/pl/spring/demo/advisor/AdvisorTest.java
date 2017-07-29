package pl.spring.demo.advisor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.dao.BookDao;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonAdvisorTest-context.xml")
public class AdvisorTest {
	
	@Autowired
	private BookDao bookDao;

	@Test
	public void shouldSaveNewBookWithNewId() {
		BookTo book = new BookTo();
		book.setTitle("Test book");
		book.setAuthors("Batman Superman");
		bookDao.save(book);
		
		Assert.assertEquals(7, bookDao.findAll().size());
		Assert.assertTrue(book.getId() == 7L);
		Assert.assertTrue(bookDao.findBookByTitle("test book").get(0).getId() == 7L);
		Assert.assertTrue(bookDao.findBooksByAuthor("batman superman").get(0).getId() == 7L);
	}

}
