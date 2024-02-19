package sit.cp23ej2.dtos.Review;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.User;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewDTO {
    private Integer reviewId;
   
    private String reviewTitle;

    private String reviewDetail;
    
    private Long reviewRating;

    private Integer spoileFlag;

    private Integer reviewTotalLike;

    private Integer reviewTotalDisLike;

    private LocalDateTime reviewCreateDateTime;

    private LocalDateTime reviewUpdateDateTime;

    @JsonIgnore
    private Book book;
    
    @JsonIgnore
    private User user;

    private UserDTO userDetail;
}
