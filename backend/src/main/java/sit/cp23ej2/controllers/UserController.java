package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.dtos.User.ForgetPasswordDTO;
import sit.cp23ej2.dtos.User.ResetPasswordDTO;
import sit.cp23ej2.dtos.User.UpdateUserByAdminDTO;
import sit.cp23ej2.dtos.User.UpdateUserDTO;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.UserService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://capstone23.sit.kmutt.ac.th/ej2"
}, methods = {
    RequestMethod.OPTIONS,
    RequestMethod.GET,
    RequestMethod.PUT,
    RequestMethod.DELETE,
    RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@Validated
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/all")
    public DataResponse getAllUser(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(required =  false) String role,
            @RequestParam(required = false) String search) throws HandleExceptionNotFound {
        return userService.getUser(page, size, role, search);
    }

    @RequestMapping("/{userId}")
    public DataResponse getUserById(@PathVariable Integer userId) throws HandleExceptionNotFound {
        return userService.getUserById(userId);
    }

    @GetMapping("/ranking")
    public DataResponse getUserRanking(@RequestParam(defaultValue = "followers") String sort) {
        return userService.getUserRanking(sort);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse createUser(@RequestBody @Valid CreateUserDTO user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); 
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public DataResponse updateUser(@RequestPart("user") @Valid UpdateUserDTO user, @PathVariable Integer userId, @RequestPart(value = "file") @Nullable MultipartFile file) {
        // user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); 
        return userService.updateUser(user, userId, file);
    }

    @PutMapping("/resetPassword")
    public DataResponse updateUserByAdmin(@RequestBody @Valid ResetPasswordDTO user) {
        // user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); 
        return userService.resetPassword(user);
    }

    @PutMapping("/forgetPassword")
    public DataResponse forgetPassword(@RequestBody @Valid ForgetPasswordDTO forgetPassword) {
        return userService.forgetPassword(forgetPassword);
    }

    @PutMapping("/admin/{userId}")
    public DataResponse updateUserByAdmin(@RequestPart("user") @Valid UpdateUserByAdminDTO user, @PathVariable Integer userId, @RequestPart(value = "file") @Nullable MultipartFile file) {
        return userService.updateUserByAdmin(user, userId, file);
    }

    @DeleteMapping("/{userId}")
    public DataResponse deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }
       
}
