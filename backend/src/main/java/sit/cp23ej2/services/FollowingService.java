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
import sit.cp23ej2.dtos.Folloing.PageFollowerDTO;
import sit.cp23ej2.dtos.Folloing.PageFollowingDTO;
import sit.cp23ej2.dtos.User.UserFollowDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.FollowingReposiroty;
import sit.cp23ej2.repositories.NotificationRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class FollowingService extends CommonController {

    @Autowired
    private FollowingReposiroty reposiroty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

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
        PageFollowerDTO follower = modelMapper.map(reposiroty.getFollowers(pageable, user.getUserId()),
                PageFollowerDTO.class);

        if (follower.getContent().size() > 0) {
            follower.getContent().forEach(follow -> {
                UserFollowDTO userDTO = modelMapper.map(follow.getUser(), UserFollowDTO.class);
                try {
                    if (user != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDTO.getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                follow.setUserFollowers(userDTO);

                if (reposiroty.checkExists(user.getUserId(), userDTO.getUserId()) == 0) {
                    follow.setFollowStatus(0);
                } else {
                    follow.setFollowStatus(1);
                }
            });

        } else {
            throw new HandleExceptionNotFound("Followers Not Found", "Followers");
        }

        return responseWithData(follower, 200, "OK", "All Followers");
    }

    public DataResponse getFollowing(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        PageFollowingDTO follow = modelMapper.map(reposiroty.getFollowings(pageable, user.getUserId()),
                PageFollowingDTO.class);

        if (follow.getContent().size() > 0) {
            follow.getContent().forEach(follows -> {
                UserFollowDTO userDTO = modelMapper.map(follows.getUserfollowing(), UserFollowDTO.class);
                try {
                    if (user != null) {
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
        DataResponse response = new DataResponse();

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
                "followed you. Follow them back to be friend.", 0, 0, "/user/" + user.getUserId(), "Follow");
        userRepository.increaseFollowings(user.getUserId());
        userRepository.increaseFollowers(userFollowingId);
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Following Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse deleteFollowing(Integer userFollowingId) {
        DataResponse response = new DataResponse();

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
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Following Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
