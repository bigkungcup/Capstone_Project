package sit.cp23ej2.dtos.Folloing;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.User.UserFollowDTO;
import sit.cp23ej2.entities.User;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowerDTO {
    private Integer followId; 
    
    @JsonIgnore
    private User user;

    @JsonIgnore
    private User userfollow;

    // private UserFollowDTO userFollowings;

    private UserFollowDTO userFollowers;

    private Integer followStatus;

    private Integer followingStatus;

    private LocalDateTime followCreateDateTime;

    private LocalDateTime followUpdateDateTime;
}
