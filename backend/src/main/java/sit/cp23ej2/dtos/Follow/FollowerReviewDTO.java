package sit.cp23ej2.dtos.Follow;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowerReviewDTO {
    private Integer followerId; 

    private Integer followerStatus;

    private LocalDateTime followerCreateDateTime;

    private LocalDateTime followerUpdateDateTime;
    
}
