package sit.cp23ej2.controllers;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.security.LoginDTO;
import sit.cp23ej2.exception.HandleUnauthorizedException;
import sit.cp23ej2.services.JwtService;
import sit.cp23ej2.services.UserService;

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
}, allowedHeaders = "*", exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"})
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

    @Autowired
    private UserService userService;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/login")
    public DataResponse login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO)  {

        // DataResponse dataResponse = new DataResponse();
        try {
            Authentication attemptAuthentication = jwtService.attemptAuthentication(request, response, loginDTO);

            return jwtService.authenticationSuccessful(request, response, attemptAuthentication);

            // loginDTO.setPassword(new BCryptPasswordEncoder().encode(loginDTO.getPassword())); 

            // dataResponse.setResponse_code(200);
            // dataResponse.setResponse_status("OK");
            // dataResponse.setResponse_message("Login Success");
            // dataResponse.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            // dataResponse.setData(loginDTO);
            
            // return dataResponse;
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
            throw new HandleUnauthorizedException("Invalid email or password");
        }
    }

    @GetMapping("/refresh")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws Exception {
      jwtService.refreshToken(request, response, authentication);
    }

   @GetMapping("/profile")
    public DataResponse getProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userService.getUserByEmail();
    }

    // @GetMapping("/logout")
    // public DataResponse logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     return jwtService.logout(request, response);
    // }
}
