package sit.cp23ej2.dtos.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.Follow.FollowerReviewDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private Integer userId;
   
    private String displayName;

    private String email;

    private String password;

    private String role;

    private Integer followers;

    private Integer follows;

    private Integer totalReview;

    private Integer totalFavoriteReview;

    private Integer totalLike;

    private String bio;

    private String file;

    private FollowerReviewDTO followerReview;
}
