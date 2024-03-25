package sit.cp23ej2.dtos.History;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.entities.Book;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistoryDTO {
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

    // private String bookTag;
    
    // private LocalDateTime bookCreateDateTime;

    // private LocalDateTime bookUpdateDateTime;

    // private Integer bookTotalReview;

    // private String file;

    @JsonIgnore
    private Book book;

    private BookDTO bookData;

    private Long countDateTime;
}
