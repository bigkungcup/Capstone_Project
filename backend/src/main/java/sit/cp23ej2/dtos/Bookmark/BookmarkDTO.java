package sit.cp23ej2.dtos.Bookmark;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.Book.BookDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookmarkDTO {
    private Integer bookmarkId;
    
    private Integer bookmarkStatus;

    private LocalDateTime bookmarkCreateDateTime;

    private LocalDateTime bookmarkUpdateDateTime;

    private BookDTO book;

    // private User user;    
}
