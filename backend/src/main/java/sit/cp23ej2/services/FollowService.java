package sit.cp23ej2.services;

import java.nio.file.Path;
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
import sit.cp23ej2.dtos.Folloing.PageFollowerDTO;
import sit.cp23ej2.dtos.Folloing.PageFollowingDTO;
import sit.cp23ej2.dtos.User.UserFollowDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.FollowReposiroty;
import sit.cp23ej2.repositories.NotificationRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class FollowService extends CommonController {

    @Autowired
    private FollowReposiroty reposiroty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getFollowers(Integer userId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        
        PageFollowerDTO follower = modelMapper.map(reposiroty.getFollowers(pageable, userId),
                PageFollowerDTO.class);

        if (follower.getContent().size() > 0) {
            follower.getContent().forEach(follow -> {
                UserFollowDTO userDTO = modelMapper.map(follow.getUser(), UserFollowDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(userDTO.getUserId());
                    if (pathFile != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDTO.getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                follow.setUserFollowers(userDTO);

                if (reposiroty.checkExists(userId, userDTO.getUserId()) == 0) {
                    follow.setFollowingStatus(0);
                } else {
                    follow.setFollowingStatus(1);
                }
            });

        } else {
            throw new HandleExceptionNotFound("Followers Not Found", "Followers");
        }

        return responseWithData(follower, 200, "OK", "All Followers");
    }

    public DataResponse getFollowing(Integer userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // String currentPrincipalName = authentication.getName();

        // User user = userRepository.getUserByEmail(currentPrincipalName);

        PageFollowingDTO follow = modelMapper.map(reposiroty.getFollowings(pageable, userId),
                PageFollowingDTO.class);

        if (follow.getContent().size() > 0) {
            follow.getContent().forEach(follows -> {
                UserFollowDTO userDTO = modelMapper.map(follows.getUserfollow(), UserFollowDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(userDTO.getUserId());
                    if (pathFile != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDTO.getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                follows.setUserFollowings(userDTO);
            });

        } else {
            throw new HandleExceptionNotFound("Followings Not Found", "Followings");
        }

        return responseWithData(follow, 200, "OK", "All Followings");
    }

    public DataResponse insertFollowing(Integer userFollowingId) {
        // DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if (userRepository.getUserById(userFollowingId) == null) {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        if (reposiroty.checkExists(user.getUserId(), userFollowingId) != 0) {
            throw new HandleExceptionBadRequest(currentPrincipalName + " already following this user");
        }

        if (userFollowingId == user.getUserId()) {
            throw new HandleExceptionBadRequest(currentPrincipalName + " can't following yourself");
        }

        reposiroty.insertfollowing(user.getUserId(), userFollowingId, 1);

        notificationRepository.insertNotification(userFollowingId, user.getDisplayName(),
                " followed you. Follow them back to be friend.", 0, 0, "/user/" + user.getUserId(), "Follow");
        userRepository.increaseFollowings(user.getUserId());
        userRepository.increaseFollowers(userFollowingId);
        return response(201, "Created", "Following Created");
    }

    public DataResponse deleteFollowing(Integer userFollowingId) {
        // DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if (userRepository.getUserById(userFollowingId) == null) {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        if (reposiroty.checkExists(user.getUserId(), userFollowingId) == 0) {
            throw new HandleExceptionBadRequest(currentPrincipalName + " not following this user");
        }

        if (userFollowingId == user.getUserId()) {
            throw new HandleExceptionBadRequest(currentPrincipalName + " can't unfollowing yourself");
        }

        reposiroty.deletefollowing(user.getUserId(), userFollowingId);
        userRepository.decreaseFollowings(user.getUserId());
        userRepository.decreaseFollowers(userFollowingId);
        return response(200, "OK", "Following Deleted");
    }
}
