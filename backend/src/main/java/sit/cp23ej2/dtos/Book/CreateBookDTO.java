package sit.cp23ej2.dtos.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBookDTO {

    @NotNull(message = "Book Type Id shouldn't be blank or null")
    private Integer booktypeId;

    @NotBlank(message = "Book Name shouldn't be blank or null")
    @Size(max = 255, min = 1, message = "Book Name shouldn't be more than 255 characters and less than 1 characters")
    private String bookName;

    @NotBlank(message = "Author shouldn't be blank or null")
    @Size(max = 255, min = 1,  message = "Title shouldn't be more than 255 characters and less than 1 characters")
    private String author;

    // @NotBlank(message = "Book Genre shouldn't be blank or null")
    @Size(max = 255,  message = "Title shouldn't be more than 255 characters")
    private String bookTag;

    private String bookDetail;
}
