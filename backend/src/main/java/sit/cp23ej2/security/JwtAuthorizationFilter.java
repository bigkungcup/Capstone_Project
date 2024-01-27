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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/refresh")
                || request.getServletPath().equals("/api/user")) {
            filterChain.doFilter(request, response);
        } else {
            // Cookie access_token = WebUtils.getCookie(request, "access_token");

            String access_token = this.recoverToken(request);
            
            System.out.println("Access Token :" + access_token);
            if (access_token != null) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(access_token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    ExceptionResponse errors = new ExceptionResponse();

                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("error_message", exception.getMessage());

                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    errors.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                    errors.setResponse_code(HttpStatus.UNAUTHORIZED.value());
                    errors.setResponse_status("Unauthorized");
                    errors.setResponse_message("Validation");
                    errors.setPath(request.getRequestURI());
                    errors.setFiledErrors(errorMap);

                    response.setHeader("Error", exception.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public String decode(String chunks) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decode = new String(decoder.decode(chunks));
        return decode;
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
