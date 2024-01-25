package sit.cp23ej2.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import sit.cp23ej2.dtos.ExceptionResponse;
import sit.cp23ej2.exception.HandleUnauthorizedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.auth0.jwt.JWTVerifier;
import java.sql.Timestamp;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    
    @Value("${jwt.secret}")
    private String secret;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/user")) {
            filterChain.doFilter(request, response);
        } else {
            Cookie access_token = WebUtils.getCookie(request, "access_token");
           if (access_token != null) {
                System.out.println("access_token: " + access_token.getValue());
                try {
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(access_token.getValue());
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    System.out.println("ROLE: " + authorities);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    // response.setHeader("Error", exception.getMessage());
                    // response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    // Map<String, String> error = new HashMap<>();
                    // error.put("error_message", exception.getMessage());
                    // response.setContentType(APPLICATION_JSON_VALUE);
                    // new ObjectMapper().writeValue(response.getOutputStream(), error);
                    throw new HandleUnauthorizedException(exception.getMessage());
                }
            } else {
                System.out.println("access_token ELSE: " + access_token);
                ExceptionResponse errors = new ExceptionResponse();
                // HandleExceptionLogin errors;
                Map<String, String> errorMap = new HashMap<>();
                // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                errorMap.put("Unauthorized", "Not Authentication");
                // errors = new HandleExceptionLogin(sdf3.format(timestamp), HttpStatus.UNAUTHORIZED.value(),
                //         "Unauthorized", "Validation", request.getRequestURI(), errorMap);
                errors.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                errors.setResponse_code(HttpStatus.UNAUTHORIZED.value());
                errors.setResponse_status("Unauthorized");
                errors.setResponse_message("Validation");
                errors.setPath(request.getRequestURI());
                errors.setFiledErrors(errorMap);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
                filterChain.doFilter(request, response);
            }
        }
    }

    public String decode(String chunks) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decode = new String(decoder.decode(chunks));
        return decode;
    }
}
