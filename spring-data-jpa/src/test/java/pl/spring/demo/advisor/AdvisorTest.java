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
import pl.spring.demo.exception.BookAlreadyExistsException;
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
		BookTo bookWithoutAnId = new BookTo();
		bookWithoutAnId.setTitle("Test book");
		bookWithoutAnId.setAuthors("Batman");

		// when
		bookDao.save(bookWithoutAnId);

		// then
		Assert.assertEquals(7, bookDao.findAll().size());
		Assert.assertTrue(bookWithoutAnId.getId() == 7L);
		Assert.assertTrue(bookDao.findBookByTitle("test book").get(0).getId() == 7L);
		Assert.assertTrue(bookDao.findBooksByAuthor("batman").get(0).getId() == 7L);
	}

	@Test
	public void shouldThrowAnExceptionDueToNotNullIdOfTheBook() throws BookNotNullIdException {
		// given
		BookTo bookWithAnId = new BookTo();
		bookWithAnId.setTitle("A book with an Id");
		bookWithAnId.setAuthors("Batman Superman");
		bookWithAnId.setId(12L);

		// expect
		e.expect(BookNotNullIdException.class);

		// when
		bookDao.save(bookWithAnId);

		// then
		// EXCEPTION
	}

	@Test
	public void shouldThrowAnExceptionDueToAlreadyExistingBookInDatabase() throws BookAlreadyExistsException {
		// given
		BookTo existingBook = new BookTo();
		existingBook.setTitle("Zemsta");

		// expect
		e.expect(BookAlreadyExistsException.class);
		e.expectMessage("database");

		// when
		bookDao.save(existingBook);

		// then
		// EXCEPTION
	}

}
