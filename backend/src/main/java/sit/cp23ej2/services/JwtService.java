package sit.cp23ej2.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sit.cp23ej2.dtos.security.LoginDTO;
import org.springframework.security.core.userdetails.User;

@Component
@Service
public class JwtService {

        @Value("${jwt.secret}")
        private String secret;

        private final AuthenticationManager authenticationManager;

        private final Integer jwtExpirationInMs = 60 * 60 * 1000;

        private final Integer refreshExpirationDateInMs = 24 * 60 * 60 * 1000;

        public JwtService(AuthenticationManager authenticationManager) {
                this.authenticationManager = authenticationManager;
        }

        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response,
                        LoginDTO login) throws AuthenticationException {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                login.getEmail(), login.getPassword());
                return authenticationManager.authenticate(authenticationToken);
        }

        public void authenticationSuccessful(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                User user = (User) authentication.getPrincipal();
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

                String access_token = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                                .withIssuer(request.getRequestURI().toString())
                                .withClaim("roles",
                                                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toList()))
                                .sign(algorithm);

                String refresh_token = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                                .withIssuer(request.getRequestURI().toString())
                                .sign(algorithm);

                response.addCookie(createCookie("access_token", access_token, jwtExpirationInMs));
                response.addCookie(createCookie("refresh_token", refresh_token, refreshExpirationDateInMs));

        }

        public Cookie createCookie(String name, String value, Integer maxAge) {
                Cookie cookie = new Cookie(name, value);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(maxAge);
                cookie.setPath("/");
                return cookie;
        }
}
