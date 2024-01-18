package sit.cp23ej2.dtos.User;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.cp23ej2.entities.User;

@Getter
@Setter
@NoArgsConstructor
public class PageUserDTO {
    private List<User> content;
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
