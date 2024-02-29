package sit.cp23ej2.dtos.Review;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewLikeStatus {

    @NotNull(message = "userId shouldn't be null")
    private Integer userId;

    @NotNull(message = "reviewId shouldn't be null")
    private Integer reviewId;

    @NotNull(message = "likeStatus shouldn't be null")
    private Integer likeStatus;
}
