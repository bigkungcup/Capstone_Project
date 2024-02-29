package sit.cp23ej2.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "CheckLikeReview", schema = "bannarug")
public class CheckLikeReview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkLikeReviewId")
    private Integer checkLikeReviewId;

    private Integer clr_reviewId; 
    
    private Integer clu_userId; 

    private Integer likeStatus;
    
    private LocalDateTime checkLikeReviewCreateDateTime;

    private LocalDateTime checkLikeReviewUpdateDateTime;
}
