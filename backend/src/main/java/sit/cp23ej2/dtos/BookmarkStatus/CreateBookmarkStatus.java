package sit.cp23ej2.dtos.BookmarkStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateBookmarkStatus {

    @NotNull(message = "bookmarkId shouldn't be null")
    private Integer bookmarkId;

    @NotNull(message = "bookmarkStatus shouldn't be null")
    private Integer bookmarkStatus;
}
