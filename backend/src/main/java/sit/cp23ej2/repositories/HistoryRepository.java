package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.History;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  History (hu_userId, hb_bookId)" +
            "values (:userId, :bookId);", nativeQuery = true)
    Integer insertHistory(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Query(value = "SELECT h.historyId, h.hu_userId, h.hb_bookId, h.historyCreateDateTime, h.historyUpdateDateTime, " +
            " b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, COUNT(r.reviewId) as bookTotalReview" +
            " FROM History h" +
            " LEFT JOIN Book b ON h.hb_bookId = b.bookId"  +
            " LEFT JOIN Review r ON b.bookId = r.rvb_bookId" +
            " WHERE hu_userId = :userId" +
            " GROUP BY h.historyId, h.hu_userId, h.hb_bookId, h.historyCreateDateTime, h.historyUpdateDateTime" 
            , nativeQuery = true)
    Page<History> getBookHistory(Pageable pageable, @Param("userId") Integer userId);

    Boolean existsByUserIdAndBookId(Integer userId, Integer bookId);    
}
