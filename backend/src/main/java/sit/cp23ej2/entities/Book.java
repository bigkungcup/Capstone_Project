package sit.cp23ej2.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NamedStoredProcedureQuery(
    name = "insertBook",
    procedureName = "createBookAndBookId",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "bookName", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "author", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "bookGenre", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "bookDetail", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "lastInsertId", type = Integer.class)
    }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Book", schema = "bannarug")
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
    
    private LocalDateTime bookCreateDateTime;

    private LocalDateTime bookUpdateDateTime;

    private Integer bookTotalReview;
}
