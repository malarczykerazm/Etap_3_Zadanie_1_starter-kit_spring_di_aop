package pl.spring.demo.advisor;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.dao.BookDao;
import pl.spring.demo.exception.BookNotNullIdException;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "CommonAdvisorTest-context.xml" })
public class AdvisorTest {

	@Rule
	public ExpectedException e = ExpectedException.none();

	@Autowired
	private BookDao bookDao;

	@Test
	public void shouldSaveNewBookWithNewId() {
		// given
		BookTo book = new BookTo();
		book.setTitle("Test book");
		book.setAuthors("Batman Superman");

		// when
		bookDao.save(book);

		// then
		Assert.assertEquals(7, bookDao.findAll().size());
		Assert.assertTrue(book.getId() == 7L);
		Assert.assertTrue(bookDao.findBookByTitle("test book").get(0).getId() == 7L);
		Assert.assertTrue(bookDao.findBooksByAuthor("batman superman").get(0).getId() == 7L);
	}

	@Test
	public void shouldThrowAnExceptionDueToNotNullIdOfTheBook() throws BookNotNullIdException {
		// given
		BookTo book = new BookTo();
		book.setTitle("Zemsta");

		// expect
		e.expect(BookNotNullIdException.class);
		e.expectMessage("Id");

		// when
		bookDao.save(book);

		// then
		// EXCEPTION
	}

}
