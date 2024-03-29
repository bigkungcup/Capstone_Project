package sit.cp23ej2.dtos.Book;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookReportDTO {
    private Integer bookId;
    private String bookName;
    private String author;
    private String bookDetail;
    private Integer bookTotalView;
    private Double bookRating;
    private String bookTag;
    private List<String> bookTagList;
    private LocalDateTime bookCreateDateTime;
    private LocalDateTime bookUpdateDateTime;
    // private Long countDateTime;
    private String file;
    // private Integer bookTotalReview;
    // private Booktype booktype;
    // private BookmarkBookDTO bookmark;
    // private Integer bookTotalBookmark;
}
