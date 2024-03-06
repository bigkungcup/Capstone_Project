package sit.cp23ej2.dtos.Folloing;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FollowingReviewDTO {
    private Integer followingId; 

    private Integer followingStatus;

    private LocalDateTime followingCreateDateTime;

    private LocalDateTime followingUpdateDateTime;
    
}
