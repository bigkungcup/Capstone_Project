package sit.cp23ej2.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "History", schema = "bannarug")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyId")
    private Integer historyId;

    private Integer hu_userId;

    private LocalDateTime historyCreateDateTime;

    private LocalDateTime historyUpdateDateTime;

    // private Integer bookId;
   
    // private String bookName;

    // private String author;
    
    // private String bookDetail;

    // private Integer bookTotalView;

    // private Long bookRating;

    // private String bookGenre;
    
    // private LocalDateTime bookCreateDateTime;

    // private LocalDateTime bookUpdateDateTime;

    // private Integer bookTotalReview;

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hb_bookId", nullable = true)
    private Book book;
}
