package sit.cp23ej2.repositories;

import sit.cp23ej2.entities.Book;

// import java.util.HashMap;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookRepository  extends JpaRepository<Book, Integer>{

    @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime FROM Book b", nativeQuery = true)
    List<Book> getAllBooks();

    @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime FROM Book b WHERE bookId = :bookId", nativeQuery = true)
    Book findBookById(@Param("bookId") int bookId);

}
