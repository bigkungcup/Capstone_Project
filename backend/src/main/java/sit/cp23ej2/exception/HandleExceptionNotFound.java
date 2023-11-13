package sit.cp23ej2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionNotFound extends RuntimeException{
    private String key;

    public HandleExceptionNotFound(String message, String key) {
        super(message);
        this.key = key;
    }

    public HandleExceptionNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
