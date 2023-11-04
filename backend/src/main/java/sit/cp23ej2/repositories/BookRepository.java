package sit.cp23ej2.repositories;

import sit.cp23ej2.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository  extends JpaRepository<Book, Integer>{
    
}
