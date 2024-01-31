package sit.cp23ej2.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.security.LoginDTO;
import sit.cp23ej2.exception.HandleUnauthorizedException;
import sit.cp23ej2.repositories.UserRepository;

import org.springframework.security.core.userdetails.User;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Service
public class JwtService {

        @Autowired
        private UserRepository userRepository;

        @Value("${jwt.secret}")
        private String secret;

        private final AuthenticationManager authenticationManager;

        private final Integer jwtExpirationInMs =  60 * 1000;

        private final Integer refreshExpirationDateInMs =  2 * 60 * 1000;

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public JwtService(AuthenticationManager authenticationManager) {
                this.authenticationManager = authenticationManager;
        }

        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response,
                        LoginDTO login) throws AuthenticationException {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                login.getEmail(), login.getPassword());
                return authenticationManager.authenticate(authenticationToken);
        }

        public DataResponse authenticationSuccessful(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                User user = (User) authentication.getPrincipal();
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
                System.out.println("user.getUsername() = " + user.getAuthorities());
                System.out.println("user.getAuthorities() = "
                                + user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                                .collect(Collectors.toList()));

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

                // response.addCookie(createCookie("access_token", access_token,
                // jwtExpirationInMs));
                // response.addCookie(createCookie("refresh_token", refresh_token,
                // refreshExpirationDateInMs));

                DataResponse dataResponse = new DataResponse();
                Map<String, String> dataMap = new HashMap<>();

                dataMap.put("access_token", access_token);
                dataMap.put("refresh_token", refresh_token);

                dataResponse.setResponse_code(200);
                dataResponse.setResponse_status("OK");
                dataResponse.setResponse_message("Login Success");
                dataResponse.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                dataResponse.setData(dataMap);
                return dataResponse;

        }

        public void refreshToken(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                // Cookie refresh_token = WebUtils.getCookie(request, "refresh_token");
                String refresh_token = this.recoverToken(request);

                if (refresh_token != null) {
                        try {

                                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
                                JWTVerifier verifier = JWT.require(algorithm).build();
                                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                                String email = decodedJWT.getSubject();
                                sit.cp23ej2.entities.User userData = userRepository.getUserByEmail(email);

                                Collection<GrantedAuthority> authorities = new ArrayList<>();
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + userData.getRole()));

                                String access_token = JWT.create()
                                                .withSubject(userData.getEmail())
                                                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                                                .withIssuer(request.getRequestURI().toString())
                                                .withClaim("roles",
                                                                authorities.stream().map(GrantedAuthority::getAuthority)
                                                                                .collect(Collectors.toList()))
                                                .sign(algorithm);

                                // response.addCookie(createCookie("access_token", access_token,
                                // jwtExpirationInMs));
                                // response.addCookie(createCookie("refresh_token", refresh_token,
                                // refreshExpirationDateInMs));

                                Map<String, String> dataMap = new HashMap<>();

                                DataResponse dataResponse = new DataResponse();

                                dataResponse.setResponse_code(200);
                                dataResponse.setResponse_status("OK");
                                dataResponse.setResponse_message("Refresh token success");
                                dataResponse.setResponse_datetime(
                                                sdf3.format(new Timestamp(System.currentTimeMillis())));

                                dataMap.put("access_token", access_token);
                                dataMap.put("refresh_token", refresh_token);

                                dataResponse.setData(dataMap);

                                response.setStatus(HttpStatus.OK.value());
                                response.setContentType(APPLICATION_JSON_VALUE);

                                new ObjectMapper().writeValue(response.getOutputStream(), dataResponse);

                        } catch (Exception exception) {
                                DataResponse dataResponse = new DataResponse();

                                dataResponse.setResponse_code(401);
                                dataResponse.setResponse_status("Unauthorized");
                                dataResponse.setResponse_message("Refresh token is expired");
                                dataResponse.setResponse_datetime(
                                                sdf3.format(new Timestamp(System.currentTimeMillis())));

                                response.setHeader("error", exception.getMessage());
                                response.setStatus(UNAUTHORIZED.value());
                                Map<String, String> error = new HashMap<>();
                                error.put("error_message", "Refresh token is expired");
                                response.setContentType(APPLICATION_JSON_VALUE);
                                dataResponse.setData(error);
                                new ObjectMapper().writeValue(response.getOutputStream(), dataResponse);
                        }
                } else {
                        throw new HandleUnauthorizedException("Refresh token is missing");
                }

        }

        public Cookie createCookie(String name, String value, Integer maxAge) {
                Cookie cookie = new Cookie(name, value);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(maxAge);
                cookie.setPath("/");
                return cookie;
        }

        private String recoverToken(HttpServletRequest request) {
                var authHeader = request.getHeader("Refresh-Token");
                if (authHeader == null)
                        return null;
                return authHeader.replace("Bearer ", "");
        }
}
