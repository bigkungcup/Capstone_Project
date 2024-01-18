package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.UserService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

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
@Validated
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public DataResponse getAllUser(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws HandleExceptionNotFound {
        return userService.getUser(page, size);
    }

    @RequestMapping("/{userId}")
    public DataResponse getUserById(@RequestParam Integer userId) throws HandleExceptionNotFound {
        return userService.getUserById(userId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse createUser(@RequestBody @Valid CreateUserDTO user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    public DataResponse deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }
       
}
