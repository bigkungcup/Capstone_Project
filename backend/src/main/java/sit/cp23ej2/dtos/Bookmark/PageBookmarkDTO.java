package sit.cp23ej2.dtos.Bookmark;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class PageBookmarkDTO {
    private List<BookmarkDTO> content;
    private Object pageable;
    private int pageNumber;
    private int size;
    private int totalPages;
    private int numberOfElements;
    private int totalElements;
    private boolean last;
    private boolean first;
    private boolean empty;
}