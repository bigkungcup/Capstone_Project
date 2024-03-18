package sit.cp23ej2.dtos.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserFollowDTO {
    private Integer userId;
   
    private String displayName;

    private String email;

    private String password;

    private String role;

    private Integer followers;

    private Integer followings;

    private Integer totalReview;

    private Integer totalFavoriteReview;

    private Integer totalLike;

    private String bio;

    private String file;

}
