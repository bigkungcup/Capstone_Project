package sit.cp23ej2.dtos.Folloing;

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
    private Integer followingId; 
    
    @JsonIgnore
    private User user;

    @JsonIgnore
    private User userfollowing;

    private UserDTO userFollowings;

    private UserDTO userFollowers;

    private Integer followingStatus;

    private LocalDateTime followingCreateDateTime;

    private LocalDateTime followingUpdateDateTime;
}
