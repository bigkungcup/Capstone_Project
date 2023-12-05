package sit.cp23ej2.entities;

// import java.time.Instant;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import jakarta.persistence.Temporal;
// import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Review", schema = "bannarug")
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

    private Integer reviewTotalDisLike;

    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime reviewCreateDateTime;

    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime reviewUpdateDateTime;

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rvb_bookId", nullable = true)
    private Book book;

}
