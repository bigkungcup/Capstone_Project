package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Follow.PageFollowDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.FollowerReposiroty;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class FollowerService extends CommonController{
    
    @Autowired
    private FollowerReposiroty reposiroty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getFollowers(Integer page, Integer size) {
       
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        PageFollowDTO follower = modelMapper.map(reposiroty.getFollowers(pageable, user.getUserId()), PageFollowDTO.class);

        if(follower.getContent().size() > 0){
            follower.getContent().forEach(follow -> {
               UserDTO userDTO = modelMapper.map(follow.getUser(), UserDTO.class);
               try {
                if (user != null) {
                    userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
                follow.setUserFollowers(userDTO);
            });

        }else{
            throw new HandleExceptionNotFound("Followers Not Found", "Followers");
        }

        return responseWithData(follower, 200, "OK", "Get Followers Success");
    }

    public DataResponse getFollowing(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        PageFollowDTO follow = modelMapper.map(reposiroty.getFollowings(pageable, user.getUserId()), PageFollowDTO.class);

        if(follow.getContent().size() > 0){
            follow.getContent().forEach(follows -> {
               UserDTO userDTO = modelMapper.map(follows.getUser(), UserDTO.class);
               try {
                if (user != null) {
                    userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
                follows.setUserFollowing(userDTO);
            });

        }else{
            throw new HandleExceptionNotFound("Follows Not Found", "Follows");
        }

        return responseWithData(follow, 200, "OK", "Get Follows Success");
    }

    public DataResponse insertFollower(Integer userFollowerId) {
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if(userRepository.getUserById(userFollowerId) == null){
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        if(reposiroty.checkExists(user.getUserId(), userFollowerId) != 0){
            throw new HandleExceptionBadRequest(currentPrincipalName + " already follow this user");
        }

        if(userFollowerId == user.getUserId()){
            throw new HandleExceptionBadRequest(currentPrincipalName + " can't follow yourself");
        }

        reposiroty.insertFollower(user.getUserId(), userFollowerId, 1);
        userRepository.increaseFollows(user.getUserId());
        userRepository.increaseFollowers(userFollowerId);
        response.setResponse_code(201);
        response.setResponse_status("Created");        
        response.setResponse_message("Insert Follower Success");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse deleteFollower(Integer userFollowerId) {
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if(userRepository.getUserById(userFollowerId) == null){
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        if(reposiroty.checkExists(user.getUserId(), userFollowerId) == 0){
            throw new HandleExceptionBadRequest(currentPrincipalName + " not follow this user");
        }

        if(userFollowerId == user.getUserId()){
            throw new HandleExceptionBadRequest(currentPrincipalName + " can't unfollow yourself");
        }

        reposiroty.deleteFollower(user.getUserId(), userFollowerId);
        userRepository.decreaseFollows(user.getUserId());
        userRepository.decreaseFollowers(userFollowerId);
        response.setResponse_code(200);
        response.setResponse_status("OK");        
        response.setResponse_message("Delete Follower Success");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
