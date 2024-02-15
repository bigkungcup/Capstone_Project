package sit.cp23ej2.exception;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import sit.cp23ej2.dtos.ExceptionResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler extends RuntimeException {

    ExceptionResponse response = new ExceptionResponse();

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { HandleExceptionNotFound.class })
    public ExceptionResponse handleException(Exception e, HandleExceptionNotFound ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put(ex.getKey(), ex.getMessage());
        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message(e.getMessage());
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionResponse handleMissingParams(MissingServletRequestParameterException ex,
            ServletWebRequest request) {

        Map<String, String> error = new HashMap<>();
        error.put(ex.getParameterName(), " is required");

        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message(ex.getParameterName() + " is required");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { jakarta.validation.ConstraintViolationException.class })
    public ExceptionResponse handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> {
            error.put(violation.getPropertyPath().toString().split("\\.")[1], violation.getMessage());
        });
        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { java.sql.SQLSyntaxErrorException.class })
    public ExceptionResponse handleSQLSyntaxErrorException(java.sql.SQLSyntaxErrorException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { java.sql.SQLException.class })
    public ExceptionResponse handleSQLErrorException(java.sql.SQLException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        if(ex.getMessage().contains("'user.displayName_UNIQUE'")){
            error.put("Error:", "Display name already exists");
        }else if(ex.getMessage().contains("'booktype.booktypeName_UNIQUE'")){
            error.put("Error:", "Booktype already exists");
        }else{
            error.put("Error:", ex.getMessage());
        }
        // error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { org.hibernate.query.UnknownParameterException.class })
    public ExceptionResponse handleUnknownParameterException(org.hibernate.query.UnknownParameterException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ExceptionResponse handleInvalidArgument(MethodArgumentNotValidException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                errors -> {
                    error.put(errors.getField(), errors.getDefaultMessage());
                });

        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { jakarta.validation.UnexpectedTypeException.class })
    public ExceptionResponse handleUnexpectedTypeException(jakarta.validation.UnexpectedTypeException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { com.fasterxml.jackson.databind.exc.InvalidDefinitionException.class })
    public ExceptionResponse handleInvalidDefinitionException(
            com.fasterxml.jackson.databind.exc.InvalidDefinitionException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class })
    public ExceptionResponse handleMethodArgumentTypeMismatchException(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { java.lang.NullPointerException.class })
    public ExceptionResponse handleNullPointerException(java.lang.NullPointerException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { sit.cp23ej2.exception.HandleExceptionFile.class })
    public ExceptionResponse handleExceptionFile(sit.cp23ej2.exception.HandleExceptionFile ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { java.nio.file.NoSuchFileException.class })
    public ExceptionResponse handleNoSuchFileException(java.nio.file.NoSuchFileException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { HandleExceptionBadRequest.class })
    public ExceptionResponse handleNoSuchFileException(HandleExceptionBadRequest ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.BAD_REQUEST.value());
        response.setResponse_status(HttpStatus.BAD_REQUEST.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { HandleUnauthorizedException.class })
    public ExceptionResponse handleUnauthorizedException(HandleUnauthorizedException ex, ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.UNAUTHORIZED.value());
        response.setResponse_status(HttpStatus.UNAUTHORIZED.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { jakarta.servlet.ServletException.class })
    public ExceptionResponse handleServletException(jakarta.servlet.ServletException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { org.springframework.security.access.AccessDeniedException.class })
    public ExceptionResponse handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { org.springframework.http.converter.HttpMessageNotReadableException.class })
    public ExceptionResponse handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex,
            ServletWebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponse_status(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {HandleExceptionForbidden.class })
    public ExceptionResponse handleExceptionForbidden(HandleExceptionForbidden ex,
            ServletWebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Map<String, String> error = new HashMap<>();
        error.put("Error:", ex.getMessage() + currentPrincipalName);
        response.setResponse_code(HttpStatus.FORBIDDEN.value());
        response.setResponse_status(HttpStatus.FORBIDDEN.name());
        response.setResponse_message("");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setFiledErrors(error);
        response.setPath(request.getRequest().getRequestURI());
        return response;
    }
}
