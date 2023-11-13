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
@Table(name = "book", schema = "bannarug")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Integer bookId;
   
    private String bookName;

    private String author;
    
    private String bookDetail;

    private Integer bookTotalView;

    private Long bookRating;

    private String bookGenre;
    
    private Instant bookCreateDateTime;

    private Instant bookUpdateDateTime;
}
