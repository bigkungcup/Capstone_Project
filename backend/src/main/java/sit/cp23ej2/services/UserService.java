package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.dtos.User.PageUserDTO;
import sit.cp23ej2.dtos.User.ResetPasswordDTO;
import sit.cp23ej2.dtos.User.UpdateUserByAdminDTO;
import sit.cp23ej2.dtos.User.UpdateUserDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class UserService extends CommonController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getUser(int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        PageUserDTO users = modelMapper.map(repository.getAllUsers(pageable), PageUserDTO.class);

        if (users.getContent().size() > 0) {
            List<UserDTO> userDTO = users.getContent();
            userDTO = userDTO.stream().map(user -> {
                try {
                    Path pathFile = fileStorageService.loadUserFile(user.getUserId());
                    if (pathFile != null) {
                        user.setFile(pathFile.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return user;
            }).collect(Collectors.toList());
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Users");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(users);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        return response;
    }

    public DataResponse getUserById(int userId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        User user = repository.getUserById(userId);
        if(user == null){
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (userDTO != null) {
            try {
                Path pathFile = fileStorageService.loadUserFile(userId);
                if (pathFile != null) {
                    userDTO.setFile(pathFile.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("User");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(userDTO);
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
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        try {
            Path pathFile = fileStorageService.loadUserFile(user.getUserId());
            if (pathFile != null) {
                userDTO.setFile(pathFile.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("User");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(userDTO);
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        return response;
    }

    public DataResponse createUser(CreateUserDTO user) {
        DataResponse response = new DataResponse();

        User userByEmail = repository.getUserByEmail(user.getEmail());
        ;

        if (userByEmail != null) {
            throw new HandleExceptionBadRequest("Email already exists");
        }

        repository.insertUser(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getBio());

        response.setResponse_code(201);
        response.setResponse_status("OK");
        response.setResponse_message("User Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse updateUser(UpdateUserDTO user, Integer userId, MultipartFile file) {
        DataResponse response = new DataResponse();
        User userById = repository.getUserById(userId);
        
        // if (new BCryptPasswordEncoder().matches(userById.getPassword(), user.getPassword())) {
        //     throw new HandleExceptionBadRequest("Password is incorrect");
        // }

        if (userById != null) {
            if (userById.getEmail().equals(user.getEmail())
                    && userById.getDisplayName().equals(user.getDisplayName())) {

                repository.updateUserDetailByUser(user.getBio(), userId);
                if (file != null) {
                    fileStorageService.deleteUserFile(userId);
                    fileStorageService.storeUserProfile(file, userId);
                }
                User dataUser = repository.getUserById(userId);
                UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                try {
                    if (user.getStatus() != null) {
                        Path pathFile = fileStorageService.loadUserFile(userId);
                        userDTO.setFile(pathFile.toString());
                    } else {
                        fileStorageService.deleteUserFile(userId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userDTO.setBio(user.getBio());
                response.setResponse_code(200);
                response.setResponse_status("OK");
                response.setResponse_message("User Updated");
                response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                response.setData(userDTO);

            } else {
                boolean existsByEmailOrDisplayName = repository.existsByEmailOrDisplayName(user.getEmail(),
                        user.getDisplayName());
                if (!existsByEmailOrDisplayName) {
                    repository.updateUser(user.getDisplayName(), user.getEmail(), user.getBio(),
                            userId);
                    if (file != null) {
                        fileStorageService.deleteUserFile(userId);
                        fileStorageService.storeUserProfile(file, userId);
                    }
                    User dataUser = repository.getUserById(userId);
                    UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                    try {
                        if (user.getStatus() != null) {
                            Path pathFile = fileStorageService.loadUserFile(userId);
                            userDTO.setFile(pathFile.toString());
                        } else {
                            fileStorageService.deleteUserFile(userId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                    userDTO.setBio(user.getBio());
                    userDTO.setDisplayName(user.getDisplayName());
                    userDTO.setEmail(user.getEmail());
                    response.setResponse_code(200);
                    response.setResponse_status("OK");
                    response.setResponse_message("User Updated");
                    response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                    response.setData(userDTO);
                } else {
                    throw new HandleExceptionBadRequest("Email or DisplayName already exists");
                }
            }
        } 
        // else {
        // throw new HandleExceptionNotFound("User Not Found", "User");
        // }

        return response;
    }

    public DataResponse updateUserByAdmin(UpdateUserByAdminDTO user, Integer userId, MultipartFile file) {
        DataResponse response = new DataResponse();
        User userById = repository.getUserById(userId);
        if (userById != null) {
            if (userById.getEmail().equals(user.getEmail())
                    && userById.getDisplayName().equals(user.getDisplayName())) {
                repository.updateUserByAdmin(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getBio(),
                        user.getRole(), userId);
                if (file != null) {
                    fileStorageService.deleteUserFile(userId);
                    fileStorageService.storeUserProfile(file, userId);
                }
                User dataUser = repository.getUserById(userId);
                UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                try {
                    if (user.getStatus() != null) {
                        Path pathFile = fileStorageService.loadUserFile(userId);
                        userDTO.setFile(pathFile.toString());
                    } else {
                        fileStorageService.deleteUserFile(userId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userDTO.setBio(user.getBio());
                userDTO.setDisplayName(user.getDisplayName());
                userDTO.setEmail(user.getEmail());
                userDTO.setRole(user.getRole());
                response.setResponse_code(200);
                response.setResponse_status("OK");
                response.setResponse_message("User Updated");
                response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                response.setData(userDTO);
            } else {
                boolean existsByEmailOrDisplayName = repository.existsByEmailOrDisplayName(user.getEmail(),
                        user.getDisplayName());
                if (!existsByEmailOrDisplayName) {
                    repository.updateUserByAdmin(user.getDisplayName(), user.getEmail(), user.getPassword(),
                            user.getBio(),
                            user.getRole(), userId);
                    if (file != null) {
                        fileStorageService.deleteUserFile(userId);
                        fileStorageService.storeUserProfile(file, userId);
                    }
                    User dataUser = repository.getUserById(userId);
                    UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                    try {
                        if (user.getStatus() != null) {
                            Path pathFile = fileStorageService.loadUserFile(userId);
                            userDTO.setFile(pathFile.toString());
                        } else {
                            fileStorageService.deleteUserFile(userId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                    userDTO.setBio(user.getBio());
                    userDTO.setDisplayName(user.getDisplayName());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setRole(user.getRole());
                    response.setResponse_code(200);
                    response.setResponse_status("OK");
                    response.setResponse_message("User Updated");
                    response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                    response.setData(userDTO);
                } else if (existsByEmailOrDisplayName && userById.getEmail().equals(user.getEmail())
                        && userById.getDisplayName().equals(user.getDisplayName())) {
                    repository.updateUserDetailByAdmin(user.getPassword(), user.getBio(), user.getRole(), userId);
                    if (file != null) {
                        fileStorageService.deleteUserFile(userId);
                        fileStorageService.storeUserProfile(file, userId);
                    }
                    User dataUser = repository.getUserById(userId);
                    UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                    try {
                        if (user.getStatus() != null) {
                            Path pathFile = fileStorageService.loadUserFile(userId);
                            userDTO.setFile(pathFile.toString());
                        } else {
                            fileStorageService.deleteUserFile(userId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                    userDTO.setBio(user.getBio());
                    userDTO.setRole(user.getRole());
                    response.setResponse_code(200);
                    response.setResponse_status("OK");
                    response.setResponse_message("User Updated");
                    response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                    response.setData(userDTO);
                } else {
                    throw new HandleExceptionBadRequest("Email or DisplayName already exists");
                }
            }

        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        return response;
    }


    public DataResponse resetPassword(ResetPasswordDTO resetPassword){
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = repository.getUserByEmail(currentPrincipalName);

        if (!new BCryptPasswordEncoder().matches(resetPassword.getPassword(), user.getPassword())) {
            throw new HandleExceptionBadRequest("Password is incorrect");
        }

        repository.resetPassword(currentPrincipalName,new BCryptPasswordEncoder().encode(resetPassword.getNewPassword()));
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Password Reset");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
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
