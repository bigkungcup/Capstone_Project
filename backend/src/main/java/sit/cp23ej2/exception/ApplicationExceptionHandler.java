package sit.cp23ej2.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import sit.cp23ej2.dtos.ExceptionResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler extends RuntimeException{
    
    ExceptionResponse response = new ExceptionResponse();

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {HandleExceptionNotFound.class})
    public ExceptionResponse handleException(Exception e, HandleExceptionNotFound ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put(ex.getKey(), ex.getMessage());
        response.setResponse_code(HttpStatus.NOT_FOUND.value());
        response.setResponse_status(HttpStatus.NOT_FOUND.name());
        response.setResponse_message(e.getMessage());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    // @ResponseStatus(HttpStatus.CREATED)
    // @ExceptionHandler(HandleExceptionNotFound.class)
    // public ExceptionResponse handleNullPointerException(HandleExceptionNotFound exception, ServletWebRequest request) {
    //     Map<String, String> error = new HashMap<>();
    //     error.put("Error:", exception.getMessage());
    //     response.setResponse_code(HttpStatus.CREATED.value());
    //     response.setResponse_status(HttpStatus.CREATED.name());
    //     response.setResponse_message(exception.getMessage());
    //     response.setResponse_datetime(Instant.now());
    //     response.setFiledErrors(error);
    //     response.setPath(request.getRequest().getRequestURI());
    //     return response;
    // }
}
