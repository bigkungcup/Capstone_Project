package sit.cp23ej2.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Integer reviewId;
   
    private String reviewTitle;

    private String reviewDetail;
    
    private Long reviewRating;

    private Integer spoileFlag;

    private Integer reviewTotalLike;
   
    private Instant bookCreateDateTime;

    private Instant bookUpdateDateTime;
}
