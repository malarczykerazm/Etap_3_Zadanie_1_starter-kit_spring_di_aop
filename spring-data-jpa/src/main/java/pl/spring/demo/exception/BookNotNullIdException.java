package pl.spring.demo.exception;

public class BookNotNullIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookNotNullIdException(String message) {
		super(message);
	}

	public BookNotNullIdException() {
	}

}
