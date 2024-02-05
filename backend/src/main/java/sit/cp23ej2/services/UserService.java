package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.dtos.User.PageUserDTO;
import sit.cp23ej2.dtos.User.UpdateUserDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class UserService extends CommonController {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getUser(int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        PageUserDTO user = modelMapper.map(repository.getAllUsers(pageable), PageUserDTO.class);

        if (user.getContent().size() > 0) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Users");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(user);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        return response;
    }

    public DataResponse getUserById(int userId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        User user = repository.getUserById(userId);
        if (user != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("User");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(user);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        return response;
    }

    public DataResponse getUserByEmail() throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = repository.getUserByEmail(currentPrincipalName);
        if (user != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("User");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(user);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        return response;
    }

    public DataResponse createUser(CreateUserDTO user) {
        DataResponse response = new DataResponse();

        User userByEmail = repository.getUserByEmail(user.getEmail());;

        if (userByEmail != null) {
            throw new HandleExceptionBadRequest("Email already exists");
        }

        repository.insertUser(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getRole(), user.getBio());

        response.setResponse_code(201);
        response.setResponse_status("OK");
        response.setResponse_message("User Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

     public DataResponse updateUser(UpdateUserDTO user, Integer userId) {
        DataResponse response = new DataResponse();
        repository.updateUser(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getBio(), userId);
        User dataUser = repository.getUserById(userId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("User Updated");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setData(dataUser);
        return response;
    }

    public DataResponse deleteUser(int userId) {
        DataResponse response = new DataResponse();

        repository.deleteUser(userId);

        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("User Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
