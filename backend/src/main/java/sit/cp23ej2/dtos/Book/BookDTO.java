package sit.cp23ej2.dtos.Book;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private String countDateTime;
}
