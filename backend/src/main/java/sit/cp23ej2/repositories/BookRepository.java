package sit.cp23ej2.repositories;

import sit.cp23ej2.entities.Book;

import java.util.List;

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

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        // "INNER JOIN bookTag bg ON b.bookTag = bg.bookTag " +
                        // " LEFT JOIN Review r ON b.bookId = r.rvb_bookId" +
                        " WHERE (:bookRating IS NULL OR b.bookRating = :bookRating) " +
                        " AND (:booktypeId IS NULL OR b.bb_booktypeId = :booktypeId)" +
                        " AND (:search IS NULL OR b.bookName LIKE %:search% OR b.author LIKE %:search%)"
        // " GROUP BY b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating,
        // b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime"
        // " CASE WHEN :sortBy = 'bookName' THEN b.bookName END ASC, " +
        // " CASE WHEN :sortBy = 'bookName' THEN b.bookName END DESC, " +
        // " CASE WHEN :sortBy = 'author' THEN b.author END ASC, " +
        // " CASE WHEN :sortBy = 'author' THEN b.author END DESC, " +
        // " CASE WHEN :sortBy = 'bookTotalView' THEN b.bookTotalView END ASC, " +
        // " CASE WHEN :sortBy IS NOT NULL AND :sortBy = 'bookTotalView' THEN
        // b.bookTotalView END ASC, " +
        // " CASE WHEN :sortBy = 'bookRating' THEN b.bookRating END ASC, " +
        // " CASE WHEN :sortBy = 'bookRating' THEN b.bookRating END DESC, " +
        // " CASE WHEN :sortBy = 'bookTag' THEN b.bookTag END ASC, " +
        // " CASE WHEN :sortBy = 'bookTag' THEN b.bookTag END DESC, " +
        // " CASE WHEN :sortBy = '' THEN b.bookCreateDateTime END DESC, " +
        // " CASE WHEN :sortBy = 'bookCreateDateTime' THEN b.bookCreateDateTime END DESC
        // " +
        // " CASE WHEN :sortBy = 'bookUpdateDateTime' THEN b.bookUpdateDateTime END ASC,
        // " +
        // " CASE WHEN :sortBy = 'bookUpdateDateTime' THEN b.bookUpdateDateTime END
        // DESC, " +
        // " CASE WHEN :sortBy = 'bookId' THEN b.bookId END ASC, " +
        // " CASE WHEN :sortBy = 'bookId' THEN b.bookId END DESC, " +
        // " CASE WHEN :sortBy IS NULL THEN b.bookId END DESC "
        // " ELSE b.bookName "
                        , nativeQuery = true)
        Page<Book> getAllBooks(Pageable pageable, @Param("bookRating") Long bookRating,
                        @Param("booktypeId") Long booktypeId, @Param("search") String search);

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        // " LEFT JOIN Review r ON b.bookId = r.rvb_bookId" +
                        " WHERE bookId = :bookId"
        // " GROUP BY b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating,
        // b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime"
                        , nativeQuery = true)
        Book getBookById(@Param("bookId") int bookId);

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " WHERE (:booktypeId IS NULL OR b.bb_booktypeId = :booktypeId) " +
                        " AND b.bookTotalView > 0 " +
                        // " AND (:bookTotalReview ) " +
                        // " ORDER By b.bookRating DESC, b.bookTotalReview DESC, b.bookName ASC LIMIT 10"
                        " ORDER BY CASE WHEN :sort_param = 'totalReview' THEN b.bookTotalReview END DESC, " +
                        " CASE WHEN :sort_param = 'totalView' THEN b.bookTotalView END DESC "
                        , nativeQuery = true)
        List<Book> getBookRanking(@Param("booktypeId") Integer booktypeId, @Param("sort_param") String sort_param);

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " WHERE b.bookTotalView > 0 " +
                        " ORDER By b.bookTotalView DESC LIMIT 5", nativeQuery = true)
        List<Book> getBookByMostView();

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " ORDER By RAND() LIMIT 6", nativeQuery = true)
        List<Book> getBookByRandom();

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " WHERE b.bb_booktypeId = :booktypeId" +
                        " ORDER By RAND() LIMIT 10", nativeQuery = true)
        List<Book> getBookByBooktype(@Param("booktypeId") Integer booktypeId);

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " ORDER By b.bookCreateDateTime DESC LIMIT 10", nativeQuery = true)
        List<Book> getBookByCreateDateTime();

        @Query(value = "SELECT b.bookId, b.bb_booktypeId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookTag, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, b.bookTotalReview"
                        +
                        " FROM Book b" +
                        " WHERE b.bb_booktypeId = :booktypeId AND b.bookRating >= 4 " +
                        " ORDER By RAND() LIMIT 10", nativeQuery = true)
        List<Book> getBookByRecommendBook(@Param("booktypeId") Integer booktypeId);

        Boolean existsByAuthorAndBookName(String author, String bookName);

        Boolean existsByBookId(Integer bookId);

        @Query(value = "SELECT SCOPE_IDENTITY() AS [SCOPE_IDENTITY]", nativeQuery = true)
        Integer getLastId();

        @Modifying
        @Transactional
        @Query(value = " CALL createBookAndBookId(:booktypeId, :bookName, :author, :bookTag, :bookDetail, @lastInsertId); ", nativeQuery = true)

        // @Query(value = "INSERT INTO Book (bookName,author,bookTag,bookDetail,
        // bookTotalView, bookRating)" +
        // "values (:bookName, :author, :bookTag, :bookDetail, 0, 0);", nativeQuery =
        // true)

        // @Query(value = "INSERT INTO Book (bookName, author, bookTag, bookDetail,
        // bookTotalView, bookRating) " +
        // "VALUES (:bookName, :author, :bookTag, :bookDetail, 0, 0);", nativeQuery =
        // true)
        Integer insertBook(@Param("booktypeId") Integer booktypeId, @Param("bookName") String bookName,
                        @Param("author") String author,
                        @Param("bookTag") String bookTag, @Param("bookDetail") String bookDetail);

        @Query(value = "SELECT @lastInsertId as id", nativeQuery = true)
        Integer getLastInsertId();

        @Modifying
        @Transactional
        @Query(value = "UPDATE Book SET bb_booktypeId =:booktypeId, bookName = :bookName, author = :author, bookTag = :bookTag, bookDetail = :bookDetail, bookUpdateDateTime = :updateDateTime"
                        +
                        " WHERE bookId = :bookId", nativeQuery = true)
        void updateBook(@Param("booktypeId") Integer booktypeId, @Param("bookName") String bookName,
                        @Param("author") String author,
                        @Param("bookTag") String bookTag, @Param("bookDetail") String bookDetail,
                        @Param("updateDateTime") String updateDateTime,
                        @Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Book SET bookTotalView = bookTotalView + 1  WHERE bookId = :bookId", nativeQuery = true)
        void increaseBookTotalView(@Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Book SET bookTotalReview = bookTotalReview + 1  WHERE bookId = :bookId", nativeQuery = true)
        void increaseBookTotalReview(@Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Book SET bookTotalReview = bookTotalReview - 1  WHERE bookId = :bookId", nativeQuery = true)
        void decreaseBookTotalReview(@Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM Book WHERE bookId = :bookId", nativeQuery = true)
        Integer deleteBook(@Param("bookId") Integer bookId);

        @Modifying
        @Transactional
        @Query(value = " CALL updateBookRating(:bookId); ", nativeQuery = true)
        void updateBookRating(@Param("bookId") Integer bookId);
}
