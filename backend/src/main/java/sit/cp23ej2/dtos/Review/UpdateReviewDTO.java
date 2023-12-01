package sit.cp23ej2.dtos.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReviewDTO {
    // private Integer reviewId;
    private Long rating;
    private String detail;
    private String title;
    private Integer spoileFlag;
    // private Integer totalLike;
    // private Integer totalDisLike;
}
