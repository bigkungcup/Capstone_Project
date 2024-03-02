package sit.cp23ej2.dtos.Bookmark;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookmarkBookDTO {
    private Integer bookmarkId;

    private Integer bookmarkStatus;

    private LocalDateTime bookmarkCreateDateTime;

    private LocalDateTime bookmarkUpdateDateTime;
    
}
