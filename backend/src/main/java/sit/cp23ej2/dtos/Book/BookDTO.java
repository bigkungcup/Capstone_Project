package sit.cp23ej2.dtos.Book;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookDTO {
    private Integer bookId;
    private String bookName;
    private String author;
    private String bookDetail;
    private Integer bookTotalView;
    private Long bookRating;
    private String bookGenre;
    private Instant bookCreateDateTime;
    private LocalDateTime bookUpdateDateTime;
    private Long countDateTime;
    private String file;
}
