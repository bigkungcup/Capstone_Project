package sit.cp23ej2.exception;

public class HandleExceptionForbidden extends RuntimeException {

    public HandleExceptionForbidden(String message) {
        super(message);
    }

    public HandleExceptionForbidden(String message, Throwable cause) {
        super(message, cause);
    }
}
