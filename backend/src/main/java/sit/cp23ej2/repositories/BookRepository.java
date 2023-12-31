package sit.cp23ej2.repositories;

import sit.cp23ej2.entities.Book;

// import java.util.HashMap;
// import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime FROM Book b", nativeQuery = true)
    Page<Book> getAllBooks(Pageable pageable);

    @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime FROM Book b WHERE bookId = :bookId", nativeQuery = true)
    Book findBookById(@Param("bookId") int bookId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  Book (bookName,author,bookGenre,bookDetail, bookTotalView, bookRating)" +
            "values (:bookName, :author, :bookGenre, :bookDetail, 0, 0);", nativeQuery = true)
    void insertBook(@Param("bookName") String bookName, @Param("author") String author,
            @Param("bookGenre") String bookGenre, @Param("bookDetail") String bookDetail);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Book SET bookName = :bookName, author = :author, bookGenre = :bookGenre, bookDetail = :bookDetail,  bookTotalView = :bookTotalView, bookRating = :bookRating " +
            " WHERE bookId = :bookId;", nativeQuery = true)
    void updateBook(@Param("bookName") String bookName, @Param("author") String author, @Param("bookGenre") String bookGenre, @Param("bookDetail") String bookDetail, 
                    @Param("bookTotalView") Integer bookTotalView, @Param("bookRating") Long bookRating, @Param("bookId") Integer bookId);

    @Query(value = "DELETE FROM Book WHERE bookId = :bookId", nativeQuery = true)
    void deleteBook(@Param("bookId") Integer bookId);
}
