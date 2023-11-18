package sit.cp23ej2.dtos.Review;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewDTO {
    private Long rating;
    private String detail;
    private String title;
    private Integer userId;
    private Integer bookId;
}
