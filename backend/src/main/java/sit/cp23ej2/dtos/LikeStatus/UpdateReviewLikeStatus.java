package sit.cp23ej2.dtos.LikeStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateReviewLikeStatus {

    // @NotNull(message = "userId shouldn't be null")
    // private Integer userId;

    @NotNull(message = "reviewId shouldn't be null")
    private Integer reviewId;

    @NotNull(message = "likeStatus shouldn't be null")
    private Integer likeStatus;
}
