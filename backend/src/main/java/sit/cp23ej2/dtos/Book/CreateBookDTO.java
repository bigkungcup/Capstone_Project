package sit.cp23ej2.dtos.Book;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBookDTO {
    @NotBlank(message = "Book Name shouldn't be blank or null")
    private String bookName;

    @NotBlank(message = "Author shouldn't be blank or null")
    private String author;

    @NotBlank(message = "Book Genre shouldn't be blank or null")
    private String bookGenre;

    private String bookDetail;
}
