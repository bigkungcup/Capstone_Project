package sit.cp23ej2.dtos.FollowerStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateFollowerStatus {

    // @NotNull(message = "userId shouldn't be null")
    // private Integer userId;

    @NotNull(message = "followerId shouldn't be null")
    private Integer followerId;

    @NotNull(message = "followerStatus shouldn't be null")
    private Integer followerStatus;
    
}
