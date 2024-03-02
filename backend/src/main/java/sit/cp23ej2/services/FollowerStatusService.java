package sit.cp23ej2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.FollowerStatus.CreateFollowerStatus;
import sit.cp23ej2.dtos.FollowerStatus.UpdateFollowerStatus;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.FollowerStatusRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class FollowerStatusService extends CommonController {

    @Autowired
    private FollowerStatusRepository repository;

    @Autowired
    private UserRepository userRepository;

    public DataResponse insertFollowerStatus(CreateFollowerStatus createFollowerStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        repository.getFollowerStatus(user.getUserId()).forEach(followerStatus -> {
            if (followerStatus.getFsu_userFollowerId().equals(createFollowerStatus.getFollowerId())) {
                throw new HandleExceptionBadRequest("You already follow this user");
            }
        });

        Long existsByFollowerId = repository.checkExistsByUserFollowerId(createFollowerStatus.getFollowerId(), user.getUserId());

        if (existsByFollowerId == 0) {
            throw new HandleExceptionNotFound("Follower Status Not Found", "Follower Status");
        }

        repository.insertFollowerStatus(createFollowerStatus.getFollowerId(), user.getUserId(), createFollowerStatus.getFollowerStatus());
        return response(201, "Created", "Follower Status Created");
    }

    public DataResponse updateFollowerStatus(Integer followerStatusId, UpdateFollowerStatus updateFollowerStatus) {
        boolean existsByFollowerStatusId = repository.existsByFollowerStatusId(followerStatusId);

        if (!existsByFollowerStatusId) {
            throw new HandleExceptionNotFound("Follower Status Not Found", "Follower Status");
        }

        if(updateFollowerStatus.getFollowerStatus() == 1){
            repository.updateFollowerStatus(updateFollowerStatus.getFollowerStatus(), followerStatusId);
            return response(200, "Updated", "Follower Status Updated");
        }else{
            repository.deleteFollowerStatus(followerStatusId);
            return response(200, "Deleted", "Follower Status Deleted");
        }
    }

}
