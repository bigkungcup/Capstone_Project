package sit.cp23ej2.dtos.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.Folloing.FollowingReviewDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserReviewDTO {
    private Integer userId;
   
    private String displayName;

    private String email;

    private Integer followers;

    private Integer followings;

    private Integer totalReview;

    private Integer totalFavoriteReview;

    private Integer totalLike;

    private String bio;

    private String file;

    private FollowingReviewDTO followingReview;
}
