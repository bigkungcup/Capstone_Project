package sit.cp23ej2.dtos.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBookDTO {
    // private Integer bookId;
    private String bookName;
    private String author;
    private String bookGenre;
    private String bookDetail;
    private Integer bookTotalView;
    private Long bookRating;
}
