package sit.cp23ej2.dtos.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReviewDTO {

    @NotNull(message = "Rating shouldn't be null")
    private Long rating;

    @NotBlank(message = "Detail shouldn't be blank or null")
    private String detail;

    @NotBlank(message = "Title shouldn't be blank or null")
    @Max(value = 255, message = "Title shouldn't be more than 255 characters")
    private String title;

    @NotNull(message = "SpoileFlag shouldn't be null")
    private Integer spoileFlag;
}
