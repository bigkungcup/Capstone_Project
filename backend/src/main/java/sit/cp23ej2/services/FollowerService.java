package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.repositories.FollowerReposiroty;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class FollowerService {
    
    @Autowired
    private FollowerReposiroty followerReposiroty;

    @Autowired
    private UserRepository userRepository;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse insertFollower(Integer userFollowerId) {
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        followerReposiroty.insertFollower(user.getUserId(), userFollowerId);
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

        followerReposiroty.deleteFollower(user.getUserId(), userFollowerId);
        userRepository.decreaseFollows(user.getUserId());
        userRepository.decreaseFollowers(userFollowerId);
        response.setResponse_code(200);
        response.setResponse_status("OK");        
        response.setResponse_message("Delete Follower Success");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
