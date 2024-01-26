package sit.cp23ej2.security;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sit.cp23ej2.dtos.ExceptionResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        ExceptionResponse errors = new ExceptionResponse();

        Map<String, String> errorMap = new HashMap<>();
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        errorMap.put("Unauthorized", "Not Authentication");

        errors.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        errors.setResponse_code(HttpStatus.UNAUTHORIZED.value());
        errors.setResponse_status("Unauthorized");
        errors.setResponse_message("Validation");
        errors.setPath(request.getRequestURI());
        errors.setFiledErrors(errorMap);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }
}