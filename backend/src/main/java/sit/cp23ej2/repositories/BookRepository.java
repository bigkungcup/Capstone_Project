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

        @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, COUNT(r.reviewId) as bookTotalReview"  +
                        " FROM Book b" +
                        // "INNER JOIN BookGenre bg ON b.bookGenre = bg.bookGenre " +
                        " LEFT JOIN Review r ON b.bookId = r.rvb_bookId" +
                        " WHERE (:bookRating IS NULL OR b.bookRating = :bookRating)" +
                        " GROUP BY b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime" 
                        // " CASE WHEN :sortBy = 'bookName' THEN b.bookName END ASC, " +
                        // " CASE WHEN :sortBy = 'bookName' THEN b.bookName END DESC, " +
                        // " CASE WHEN :sortBy = 'author' THEN b.author END ASC, " +
                        // " CASE WHEN :sortBy = 'author' THEN b.author END DESC, " +
                        // " CASE WHEN :sortBy = 'bookTotalView' THEN b.bookTotalView END ASC, " +
                        // " CASE WHEN :sortBy IS NOT NULL AND :sortBy = 'bookTotalView' THEN b.bookTotalView END ASC, " +
                        // " CASE WHEN :sortBy = 'bookRating' THEN b.bookRating END ASC, " +
                        // " CASE WHEN :sortBy = 'bookRating' THEN b.bookRating END DESC, " +
                        // " CASE WHEN :sortBy = 'bookGenre' THEN b.bookGenre END ASC, " +
                        // " CASE WHEN :sortBy = 'bookGenre' THEN b.bookGenre END DESC, " +
                        // " CASE WHEN :sortBy = '' THEN b.bookCreateDateTime END DESC, " +
                        // " CASE WHEN :sortBy = 'bookCreateDateTime' THEN b.bookCreateDateTime END DESC " +
                        // " CASE WHEN :sortBy = 'bookUpdateDateTime' THEN b.bookUpdateDateTime END ASC, " +
                        // " CASE WHEN :sortBy = 'bookUpdateDateTime' THEN b.bookUpdateDateTime END DESC, " +
                        // " CASE WHEN :sortBy = 'bookId' THEN b.bookId END ASC, " +
                        // " CASE WHEN :sortBy = 'bookId' THEN b.bookId END DESC, " +
                        // " CASE WHEN :sortBy IS NULL THEN b.bookId END DESC "
                        // " ELSE b.bookName "
                        , nativeQuery = true)
        Page<Book> getAllBooks(Pageable pageable, @Param("bookRating") Long bookRating);

        @Query(value = "SELECT b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime FROM Book b WHERE bookId = :bookId", nativeQuery = true)
        Book getBookById(@Param("bookId") int bookId);

        Boolean existsByAuthorAndBookName(String author, String bookName);

        @Query(value = "SELECT SCOPE_IDENTITY() AS [SCOPE_IDENTITY]", nativeQuery = true)
        Integer getLastId();

        @Modifying
        @Transactional
        // @Query(value =
        // " CALL createBookAndBookId(:bookName, :author, :bookGenre, :bookDetail); "
        // , nativeQuery = true)
        // @Query(value = "INSERT INTO Book (bookName,author,bookGenre,bookDetail,
        // bookTotalView, bookRating)" +
        // "values (:bookName, :author, :bookGenre, :bookDetail, 0, 0);", nativeQuery =
        // true)
        @Query(value = "INSERT INTO Book (bookName, author, bookGenre, bookDetail, bookTotalView, bookRating) " +
                        "VALUES (:bookName, :author, :bookGenre, :bookDetail, 0, 0);", nativeQuery = true)
        Integer insertBook(@Param("bookName") String bookName, @Param("author") String author,
                        @Param("bookGenre") String bookGenre, @Param("bookDetail") String bookDetail);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Book SET bookName = :bookName, author = :author, bookGenre = :bookGenre, bookDetail = :bookDetail, bookRating = :bookRating "
                        +
                        " WHERE bookId = :bookId", nativeQuery = true)
        void updateBook(@Param("bookName") String bookName, @Param("author") String author,
                        @Param("bookGenre") String bookGenre, @Param("bookDetail") String bookDetail,
                        @Param("bookRating") Long bookRating, @Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM Book WHERE bookId = :bookId", nativeQuery = true)
        Integer deleteBook(@Param("bookId") Integer bookId);
}
