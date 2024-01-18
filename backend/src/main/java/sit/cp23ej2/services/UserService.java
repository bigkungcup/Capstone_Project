package sit.cp23ej2.services;

import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.dtos.User.PageUserDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class UserService extends CommonController {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public DataResponse getUser(int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        PageUserDTO user = modelMapper.map(repository.getAllUsers(pageable), PageUserDTO.class);

        if (user.getContent().size() > 0) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Users");
            response.setResponse_datetime(Instant.now());
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
            response.setResponse_datetime(Instant.now());
            response.setData(user);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        return response;
    }

    public DataResponse createUser(CreateUserDTO user) {
        DataResponse response = new DataResponse();

        repository.insertUser(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getRole(), user.getBio());

        response.setResponse_code(201);
        response.setResponse_status("OK");
        response.setResponse_message("User Created");
        response.setResponse_datetime(Instant.now());
        return response;
    }

    public DataResponse deleteUser(int userId) {
        DataResponse response = new DataResponse();

        repository.deleteUser(userId);

        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("User Deleted");
        response.setResponse_datetime(Instant.now());
        return response;
    }
}
