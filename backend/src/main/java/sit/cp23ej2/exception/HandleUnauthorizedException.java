package sit.cp23ej2.exception;

public class HandleUnauthorizedException extends RuntimeException{

    public HandleUnauthorizedException(String message) {
		super(message);
	}

	public HandleUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
}
