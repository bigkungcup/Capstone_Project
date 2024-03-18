package sit.cp23ej2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionBadRequest extends RuntimeException{
    public HandleExceptionBadRequest(String message) {
		super(message);
	}

	public HandleExceptionBadRequest(String message, Throwable cause) {
		super(message, cause);
	}
}
