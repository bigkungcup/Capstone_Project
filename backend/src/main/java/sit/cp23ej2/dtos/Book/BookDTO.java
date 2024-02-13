package sit.cp23ej2.dtos.Book;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.entities.Booktype;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookDTO {
    private Integer bookId;
    // private Integer bookTypeId;
    private String bookName;
    private String author;
    private String bookDetail;
    private Integer bookTotalView;
    private Long bookRating;
    private String bookTag;
    private List<String> bookTagList;
    private LocalDateTime bookCreateDateTime;
    private LocalDateTime bookUpdateDateTime;
    private Long countDateTime;
    private String file;
    private Integer bookTotalReview;
    private Booktype booktype;
}
