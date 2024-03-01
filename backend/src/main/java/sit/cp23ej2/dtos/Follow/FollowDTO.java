package sit.cp23ej2.dtos.Follow;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.User;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowDTO {
    private Integer followerId; 
    
    @JsonIgnore
    private User user;

    @JsonIgnore
    private User userFollower;

    private UserDTO userFollowing;

    private UserDTO userFollowers;

    private LocalDateTime followerCreateDateTime;

    private LocalDateTime followerUpdateDateTime;
}
