package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Folloing.FollowingReviewDTO;
import sit.cp23ej2.dtos.User.CreateUserDTO;
import sit.cp23ej2.dtos.User.ForgetPasswordDTO;
import sit.cp23ej2.dtos.User.PageUserDTO;
import sit.cp23ej2.dtos.User.ResetPasswordDTO;
import sit.cp23ej2.dtos.User.UpdateUserByAdminDTO;
import sit.cp23ej2.dtos.User.UpdateUserDTO;
import sit.cp23ej2.dtos.User.UserByIdDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.dtos.User.UserRankingDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.FollowReposiroty;
import sit.cp23ej2.repositories.ReviewRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class UserService extends CommonController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FollowReposiroty followReposiroty;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getUser(int page, int size, String role, String search) throws HandleExceptionNotFound {
        // DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        if(role == null){
            PageUserDTO users = modelMapper.map(repository.getAllUsers(pageable, role, search), PageUserDTO.class);

            if (users.getContent().size() > 0) {
                List<UserDTO> userDTO = users.getContent();
                userDTO = userDTO.stream().map(user -> {
                    try {
                        Path pathFile = fileStorageService.loadUserFile(user.getUserId());
                        if (pathFile != null) {
                            user.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return user;
                }).collect(Collectors.toList());
                return responseWithData(users, 200, "OK", "All Users");
            } else {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }
        }else if(role.equals("USER") || role.equals("ADMIN")){
            PageUserDTO users = modelMapper.map(repository.getAllUsers(pageable, role, search), PageUserDTO.class);

            if (users.getContent().size() > 0) {
                List<UserDTO> userDTO = users.getContent();
                userDTO = userDTO.stream().map(user -> {
                    try {
                        Path pathFile = fileStorageService.loadUserFile(user.getUserId());
                        if (pathFile != null) {
                            // user.setFile(pathFile.toString());
                            // user.setFile("http://localhost:8080/api/files/filesUser/" +
                            // user.getUserId());
                            user.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return user;
                }).collect(Collectors.toList());
                // response.setResponse_code(200);
                // response.setResponse_status("OK");
                // response.setResponse_message("All Users");
                // response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                // response.setData(users);
                return responseWithData(users, 200, "OK", "All Users");
            } else {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }
        }else{
            throw new HandleExceptionBadRequest("Role is incorrect");
        }

       
    }

    public DataResponse getUserById(int userId) throws HandleExceptionNotFound {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = repository.getUserByEmail(currentPrincipalName);

        if(user == null){
            User userDetail = repository.getUserById(userId);
            if (userDetail == null) {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }

            UserByIdDTO userDTO = modelMapper.map(userDetail, UserByIdDTO.class);
            try {
                Path pathFile = fileStorageService.loadUserFile(userId);
                if (pathFile != null) {
                    userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDetail.getUserId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseWithData(userDTO, 200, "OK", "User");
        }

        if(user.getUserId().equals(userId)){
            User userDetail = repository.getUserById(user.getUserId());

            if (userDetail == null) {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }

            UserDTO userDTO = modelMapper.map(userDetail, UserDTO.class);
            if (userDTO != null) {
                try {
                    Path pathFile = fileStorageService.loadUserFile(userId);
                    if (pathFile != null) {
                        // userDTO.setFile(pathFile.toString());
                        // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
                        // user.getUserId());
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDetail.getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseWithData(userDTO, 200, "OK", "User");
            } else {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }
        }else if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            User userDetail = repository.getUserById(userId);
            if (userDetail == null) {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }
            UserDTO userDTO = modelMapper.map(userDetail, UserDTO.class);
            if (userDTO != null) {
                try {
                    Path pathFile = fileStorageService.loadUserFile(userId);
                    if (pathFile != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDetail.getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseWithData(userDTO, 200, "OK", "User");
            } else {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }
        }else{
            User userDetail = repository.getUserById(userId);
            if (userDetail == null) {
                throw new HandleExceptionNotFound("User Not Found", "User");
            }

            UserByIdDTO userDTO = modelMapper.map(userDetail, UserByIdDTO.class);
            try {
                Path pathFile = fileStorageService.loadUserFile(userId);
                if (pathFile != null) {
                    userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDetail.getUserId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            followReposiroty.getFollowingList(user.getUserId()).forEach(following -> {
                FollowingReviewDTO folloingReview = modelMapper.map(following, FollowingReviewDTO.class);
                if (userDTO.getUserId() == following.getUserfollow().getUserId()) {
                    userDTO.setFollow(folloingReview);
                }
            });

            return responseWithData(userDTO, 200, "OK", "User");
        }
    }

    public DataResponse getUserByEmail() throws HandleExceptionNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = repository.getUserByEmail(currentPrincipalName);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        try {
            Path pathFile = fileStorageService.loadUserFile(user.getUserId());
            if (pathFile != null) {
                // userDTO.setFile(pathFile.toString());
                // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
                // user.getUserId());
                userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            // response.setResponse_code(200);
            // response.setResponse_status("OK");
            // response.setResponse_message("User");
            // response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            // response.setData(userDTO);
            return responseWithData(userDTO, 200, "OK", "User");
            
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
    }

    public DataResponse getUserRanking(String sort) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User userDetail = repository.getUserByEmail(currentPrincipalName);
      
        List<User> userRanking = repository.getUserRanking(sort);

        if (userRanking != null) {
            List<UserRankingDTO> userRankingList = userRanking.stream().map(user -> {
                UserRankingDTO userDTO = modelMapper.map(user, UserRankingDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(user.getUserId());
                    if (pathFile != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (userDetail != null) {
                    // System.out.println("userDetail"+ userDetail.getUserId());
                    // List<Follow> followingList = followReposiroty.getFollowingList(userDetail.getUserId());
                    // // if(followingList.size() > 0){
                        
                    // }
                    // System.out.println("followingList"+ followingList);
                    followReposiroty.getFollowingList(userDetail.getUserId()).forEach(following -> {
                        FollowingReviewDTO folloingReview = modelMapper.map(following, FollowingReviewDTO.class);
                        // System.out.println("folloingReview"+ following);
                        if (userDTO.getUserId() == following.getUserfollow().getUserId()) {
                            userDTO.setFollow(folloingReview);
                        }
                    });

                }
                return userDTO;
            }).collect(Collectors.toList());
            return responseWithData(userRankingList, 200, "OK", "User Ranking");
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
    }

    public DataResponse createUser(CreateUserDTO user) {
        User userByEmail = repository.getUserByEmail(user.getEmail());
        ;

        if (userByEmail != null) {
            throw new HandleExceptionBadRequest("Email already exists");
        }

        repository.insertUser(user.getDisplayName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getBio());
        return responseWithData(user, 201, "OK", "User Created");
    }

    public DataResponse updateUser(UpdateUserDTO updateUser, Integer userId, MultipartFile file) {
        User userById = repository.getUserById(userId);

        if (userById != null) {
            if (userById.getDisplayName().equals(updateUser.getDisplayName())
                    && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {

                // Display name is not changed
                repository.updateUserDetailBio(updateUser.getBio(), userId);
                if (file != null) {
                    fileStorageService.deleteUserFile(userId);
                    fileStorageService.storeUserProfile(file, userId);
                }

                User dataUser = repository.getUserById(userId);
                UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                try {
                    if (updateUser.getStatus() != null) {
                        Path pathFile = fileStorageService.loadUserFile(userId);
                        if (pathFile != null) {
                            userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
                        }
                    } else {
                        fileStorageService.deleteUserFile(userId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // userDTO.setPassword(new BCryptPasswordEncoder().encode(dataUser.getPassword()));
                userDTO.setBio(updateUser.getBio());
                return responseWithData(userDTO, 200, "OK", "User Updated");

            } else {
                // Display name is changed
                boolean existsByEmailOrDisplayName = repository.existsByDisplayName(updateUser.getDisplayName());
                if (!existsByEmailOrDisplayName) {
                    repository.updateUser(updateUser.getDisplayName(), userById.getEmail(), updateUser.getBio(),
                            userId);
                    if (file != null) {
                        fileStorageService.deleteUserFile(userId);
                        fileStorageService.storeUserProfile(file, userId);
                    }
                    User dataUser = repository.getUserById(userId);
                    UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                    try {
                        if (updateUser.getStatus() != null) {
                            Path pathFile = fileStorageService.loadUserFile(userId);
                            if (pathFile != null) {
                                userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
                            }
                        } else {
                            fileStorageService.deleteUserFile(userId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // userDTO.setPassword(new BCryptPasswordEncoder().encode(dataUser.getPassword()));
                    userDTO.setBio(updateUser.getBio());
                    userDTO.setDisplayName(updateUser.getDisplayName());

                    return responseWithData(userDTO, 200, "OK", "User Updated");
                } else {
                    throw new HandleExceptionBadRequest("DisplayName already exists");
                }
            }
        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
    }

    public DataResponse updateUserByAdmin(UpdateUserByAdminDTO updateUser, Integer userId, MultipartFile file) {
        User userById = repository.getUserById(userId);
        // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  

        if (userById != null) {
            if (userById.getDisplayName().equals(updateUser.getDisplayName())
                    && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                // Display name is not changed

                // try {
                //     System.out.println("Send Email");
                //     Map<String, Object> variables = new HashMap<>();
                //     variables.put("displayName", updateUser.getDisplayName());
                //     variables.put("password", updateUser.getPassword());
                //     variables.put("email", userById.getEmail());
                //     emailService.sendEmail(userById.getEmail(), "Reset password request", "Change Pasword", variables);
                // } catch (AddressException e) {
                //     System.out.println("Address Exception" + e.getMessage());
                //     e.printStackTrace();
                // } catch (MessagingException e) {
                //     System.out.println("Messaging Exception" + e.getMessage());
                //     e.printStackTrace();
                // }

                // if(!encoder.matches(updateUser.getPassword(), userById.getPassword()) || !updateUser.getPassword().equals(userById.getPassword())){
                if(!updateUser.getPassword().equals(userById.getPassword())){
                    updateUser.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword())); 
                    repository.updateUserDetailByAdmin(updateUser.getPassword(), updateUser.getBio(),
                    userId);
                }else{
                    repository.updateUserDetailBio(updateUser.getBio(), userId);
                }
                
                if (file != null) {
                    fileStorageService.deleteUserFile(userId);
                    fileStorageService.storeUserProfile(file, userId);
                }

                User dataUser = repository.getUserById(userId);
                UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                try {
                    if (updateUser.getStatus() != null) {
                        Path pathFile = fileStorageService.loadUserFile(userId);
                        if (pathFile != null) {
                            userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
                        }
                    } else {
                        fileStorageService.deleteUserFile(userId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // userDTO.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
                userDTO.setBio(updateUser.getBio());
                // userDTO.setRole(updateUser.getRole());

                return responseWithData(userDTO, 200, "OK", "User Updated");
            } else {
                // Display name is changed
                boolean existsByEmailOrDisplayName = repository.existsByDisplayName(updateUser.getDisplayName());
                if (!existsByEmailOrDisplayName) {
                    // if(!encoder.matches(updateUser.getPassword(), userById.getPassword())){
                    if(!updateUser.getPassword().equals(userById.getPassword())){
                        // try {
                        //     System.out.println("Send Email");
                        //     Map<String, Object> variables = new HashMap<>();
                        //     variables.put("displayName", updateUser.getDisplayName());
                        //     variables.put("password", updateUser.getPassword());
                        //     variables.put("email", userById.getEmail());
                        //     emailService.sendEmail(userById.getEmail(), "Reset password request", "Change Pasword", variables);
                        // } catch (AddressException e) {
                        //     System.out.println("Address Exception" + e.getMessage());
                        //     e.printStackTrace();
                        // } catch (MessagingException e) {
                        //     System.out.println("Messaging Exception" + e.getMessage());
                        //     e.printStackTrace();
                        // }

                        updateUser.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword())); 

                        repository.updateUserByAdmin(updateUser.getDisplayName(),
                            updateUser.getPassword(), updateUser.getBio(), userId);
                      
                    }else{
                        repository.updateUserNoPasswordByAdmin(updateUser.getDisplayName(),
                            updateUser.getBio(), userId);
                    }
                   
                    if (file != null) {
                        fileStorageService.deleteUserFile(userId);
                        fileStorageService.storeUserProfile(file, userId);
                    }
                    User dataUser = repository.getUserById(userId);
                    UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

                    try {
                        if (updateUser.getStatus() != null) {
                            Path pathFile = fileStorageService.loadUserFile(userId);
                            if (pathFile != null) {
                                userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
                            }
                        } else {
                            fileStorageService.deleteUserFile(userId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // userDTO.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
                    userDTO.setBio(updateUser.getBio());
                    // userDTO.setRole(updateUser.getRole());
                    userDTO.setDisplayName(updateUser.getDisplayName());

                    return responseWithData(userDTO, 200, "OK", "User Updated");
                } else {
                    throw new HandleExceptionBadRequest("DisplayName already exists");
                }
            }
            // if (userById.getEmail().equals(user.getEmail())
            // && userById.getDisplayName().equals(user.getDisplayName())) {
            // repository.updateUserByAdmin(user.getDisplayName(), user.getEmail(),
            // user.getPassword(), user.getBio(),
            // user.getRole(), userId);
            // if (file != null) {
            // fileStorageService.deleteUserFile(userId);
            // fileStorageService.storeUserProfile(file, userId);
            // }
            // User dataUser = repository.getUserById(userId);
            // UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

            // try {
            // if (user.getStatus() != null) {
            // Path pathFile = fileStorageService.loadUserFile(userId);
            // if (pathFile != null) {
            // // userDTO.setFile(pathFile.toString());
            // // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
            // // user.getUserId());
            // userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
            // }
            // } else {
            // fileStorageService.deleteUserFile(userId);
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            // userDTO.setBio(user.getBio());
            // userDTO.setDisplayName(user.getDisplayName());
            // userDTO.setEmail(user.getEmail());
            // userDTO.setRole(user.getRole());
            // response.setResponse_code(200);
            // response.setResponse_status("OK");
            // response.setResponse_message("User Updated");
            // response.setResponse_datetime(sdf3.format(new
            // Timestamp(System.currentTimeMillis())));
            // response.setData(userDTO);
            // } else {
            // boolean existsByEmailOrDisplayName =
            // repository.existsByEmailOrDisplayName(user.getEmail(),
            // user.getDisplayName());
            // if (!existsByEmailOrDisplayName) {
            // repository.updateUserByAdmin(user.getDisplayName(), user.getEmail(),
            // user.getPassword(),
            // user.getBio(),
            // user.getRole(), userId);
            // if (file != null) {
            // fileStorageService.deleteUserFile(userId);
            // fileStorageService.storeUserProfile(file, userId);
            // }
            // User dataUser = repository.getUserById(userId);
            // UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

            // try {
            // if (user.getStatus() != null) {
            // Path pathFile = fileStorageService.loadUserFile(userId);
            // if (pathFile != null) {
            // // userDTO.setFile(pathFile.toString());
            // // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
            // // user.getUserId());
            // userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
            // }
            // } else {
            // fileStorageService.deleteUserFile(userId);
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            // userDTO.setBio(user.getBio());
            // userDTO.setDisplayName(user.getDisplayName());
            // userDTO.setEmail(user.getEmail());
            // userDTO.setRole(user.getRole());
            // response.setResponse_code(200);
            // response.setResponse_status("OK");
            // response.setResponse_message("User Updated");
            // response.setResponse_datetime(sdf3.format(new
            // Timestamp(System.currentTimeMillis())));
            // response.setData(userDTO);
            // } else if (existsByEmailOrDisplayName &&
            // userById.getEmail().equals(user.getEmail())
            // && userById.getDisplayName().equals(user.getDisplayName())) {
            // repository.updateUserDetailByAdmin(user.getPassword(), user.getBio(),
            // user.getRole(), userId);
            // if (file != null) {
            // fileStorageService.deleteUserFile(userId);
            // fileStorageService.storeUserProfile(file, userId);
            // }
            // User dataUser = repository.getUserById(userId);
            // UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

            // try {
            // if (user.getStatus() != null) {
            // Path pathFile = fileStorageService.loadUserFile(userId);
            // if (pathFile != null) {
            // // userDTO.setFile(pathFile.toString());
            // // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
            // // user.getUserId());
            // userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);
            // }
            // } else {
            // fileStorageService.deleteUserFile(userId);
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            // userDTO.setBio(user.getBio());
            // userDTO.setRole(user.getRole());
            // response.setResponse_code(200);
            // response.setResponse_status("OK");
            // response.setResponse_message("User Updated");
            // response.setResponse_datetime(sdf3.format(new
            // Timestamp(System.currentTimeMillis())));
            // response.setData(userDTO);
            // } else if (existsByEmailOrDisplayName &&
            // userById.getEmail().equals(user.getEmail())
            // && userById.getDisplayName().equals(user.getDisplayName())
            // &&
            // SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            // .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // repository.updateUserDetailByAdmin(user.getPassword(), user.getBio(),
            // user.getRole(), userId);

            // if (file != null) {
            // fileStorageService.deleteUserFile(userId);
            // fileStorageService.storeUserProfile(file, userId);
            // }
            // User dataUser = repository.getUserById(userId);
            // UserDTO userDTO = modelMapper.map(dataUser, UserDTO.class);

            // try {
            // if (user.getStatus() != null) {
            // Path pathFile = fileStorageService.loadUserFile(userId);
            // if (pathFile != null) {
            // // userDTO.setFile(pathFile.toString());
            // // userDTO.setFile("http://localhost:8080/api/files/filesUser/" +
            // // user.getUserId());
            // userDTO.setFile(baseUrl + "/api/files/filesUser/" + userId);

            // }
            // // userDTO.setFile(pathFile.toString());
            // } else {
            // fileStorageService.deleteUserFile(userId);
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            // userDTO.setBio(user.getBio());
            // userDTO.setRole(user.getRole());
            // response.setResponse_code(200);
            // response.setResponse_status("OK");
            // response.setResponse_message("User Updated");
            // response.setResponse_datetime(sdf3.format(new
            // Timestamp(System.currentTimeMillis())));
            // response.setData(userDTO);
            // } else {
            // throw new HandleExceptionBadRequest("Email or DisplayName already exists");
            // }
            // }

        } else {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
    }

    public DataResponse resetPassword(ResetPasswordDTO resetPassword) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = repository.getUserByEmail(currentPrincipalName);

        if (!new BCryptPasswordEncoder().matches(resetPassword.getPassword(), user.getPassword())) {
            throw new HandleExceptionBadRequest("Password is incorrect");
        }

        repository.resetPassword(currentPrincipalName,
                new BCryptPasswordEncoder().encode(resetPassword.getNewPassword()));
        return response(200, "OK", "Password Reset");
    }

    public DataResponse forgetPassword(ForgetPasswordDTO forgetPassword) {

        User user = repository.getUserByEmail(forgetPassword.getEmail());

        if (user == null) {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        repository.resetPassword(forgetPassword.getEmail(), new BCryptPasswordEncoder().encode(forgetPassword.getPassword()));

        return response(200, "OK", "Forget Password");
    }

    public DataResponse deleteUser(int userId) {
        User userById = repository.getUserById(userId);

        if(userById == null){
            throw new HandleExceptionNotFound("User Not Found", "User");
        }
        
        // try {
        //     System.out.println("Send Email");
        //     Map<String, Object> variables = new HashMap<>();
        //     variables.put("username", userById.getDisplayName());
        //     variables.put("email", userById.getEmail());

        //     emailService.sendEmail(repository.getUserById(userId).getEmail(), "Your Bannarug account has been deleted.", "Delete Account", variables);
        // } catch (AddressException e) {
        //     System.out.println("Address Exception" + e.getMessage());
        //     e.printStackTrace();
        // } catch (MessagingException e) {
        //     System.out.println("Messaging Exception" + e.getMessage());
        //     e.printStackTrace();
        // }

       reviewRepository.getReviewByUserId(userId).forEach(review -> {
            // reviewRepository.deleteReview(review.getReviewId());
            bookRepository.decreaseBookTotalReview(review.getBook().getBookId());
            bookRepository.updateBookRating(review.getBook().getBookId());
        });
        
        repository.deleteUser(userId);

        fileStorageService.deleteUserFile(userId);

        return response(200, "OK", "User Deleted");
    }
}
