package pl.spring.demo.exception;

public class WrongInputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongInputException(String message) {
		super(message);
	}

	public WrongInputException() {
	}

}
