package training.week13.hongxing.manager;

public class ManageException extends Exception {
	public ManageException(Throwable cause) {
		super(cause);
	}

	public ManageException(String message) {
		super(message);
	}

	public ManageException(String message, Throwable cause) {
		super(message, cause);
	}
}
