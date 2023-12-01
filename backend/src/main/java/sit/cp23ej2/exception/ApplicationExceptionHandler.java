package sit.cp23ej2.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionResponse handleMissingParams(MissingServletRequestParameterException ex, ServletWebRequest request) {
       
        Map<String, String> error = new HashMap<>();
        error.put(ex.getParameterName(), " is required");

        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message(ex.getParameterName() + " is required");
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {jakarta.validation.ConstraintViolationException.class})
    public ExceptionResponse handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> {
            error.put(violation.getPropertyPath().toString().split("\\.")[1], violation.getMessage());
        });
        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        // response.setResponse_message(ex.getMessage());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {java.sql.SQLSyntaxErrorException.class})
    public ExceptionResponse handleSQLSyntaxErrorException(java.sql.SQLSyntaxErrorException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        // response.setResponse_message(ex.getMessage());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {java.sql.SQLException.class})
    public ExceptionResponse handleSQLErrorException(java.sql.SQLException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        // response.setResponse_message(ex.getMessage());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {org.hibernate.query.UnknownParameterException.class})
    public ExceptionResponse handleUnknownParameterException(org.hibernate.query.UnknownParameterException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        // response.setResponse_message(ex.getMessage());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ExceptionResponse handleInvalidArgument(MethodArgumentNotValidException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                errors -> {
                    error.put(errors.getField(), errors.getDefaultMessage());
                }
        );

         response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_datetime(Instant.now());
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {jakarta.validation.UnexpectedTypeException.class})
    public ExceptionResponse handleUnexpectedTypeException(jakarta.validation.UnexpectedTypeException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        // response.setResponse_message(ex.getMessage());
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
