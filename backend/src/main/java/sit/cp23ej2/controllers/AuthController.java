package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.security.LoginReq;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleUnauthorizedException;
import sit.cp23ej2.services.JwtService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {
    "http://localhost:3000",
    "*"
}, methods = {
    RequestMethod.OPTIONS,
    RequestMethod.GET,
    RequestMethod.PUT,
    RequestMethod.DELETE,
    RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // @Autowired
    // private UserRepository userRepository;

    
    // private final AuthenticationManager authenticationManager;

    // public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
    //     this.authenticationManager = authenticationManager;
    //     this.jwtService = jwtService;

    // }

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public DataResponse login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginReq loginReq)  {

        DataResponse dataResponse = new DataResponse();
        try {
            Authentication attemptAuthentication = jwtService.attemptAuthentication(request, response, loginReq);
            jwtService.authenticationSuccessful(request, response, attemptAuthentication);
            dataResponse.setResponse_code(200);
            dataResponse.setResponse_status("OK");
            dataResponse.setResponse_message("Login Success");
            dataResponse.setResponse_datetime(java.time.Instant.now());
            dataResponse.setData(loginReq);
            return dataResponse;
        }catch (BadCredentialsException e){
            // ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            // // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            // dataResponse.setData(errorResponse);
            // return dataResponse;
            throw new HandleUnauthorizedException("Invalid email or password");
        }catch (Exception e){
            // ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            // // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            // dataResponse.setData(errorResponse);
            // return dataResponse;
            throw new HandleExceptionBadRequest(e.getMessage());
        }
    }
}
