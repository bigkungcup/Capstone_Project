package sit.cp23ej2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionFile extends RuntimeException{
    public HandleExceptionFile(String message) {
		super(message);
	}

	public HandleExceptionFile(String message, Throwable cause) {
		super(message, cause);
	}
}
