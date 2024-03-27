package sit.cp23ej2.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.History;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  History (hu_userId, hb_bookId)" +
            "values (:userId, :bookId);", nativeQuery = true)
    Integer insertHistory(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Query(value = "SELECT h.historyId, h.hu_userId, h.hb_bookId, h.historyCreateDateTime, h.historyUpdateDateTime " +
        //     " b.bookId, b.bookName, b.author, b.bookTotalView, b.bookRating, b.bookGenre, b.bookDetail, b.bookCreateDateTime, b.bookUpdateDateTime, COUNT(r.reviewId) as bookTotalReview" +
            " FROM History h" +
        //     " LEFT JOIN Book b ON h.hb_bookId = b.bookId"  +
        //     " LEFT JOIN Review r ON b.bookId = r.rvb_bookId" +
            " WHERE hu_userId = :userId" +
            " ORDER BY h.historyUpdateDateTime DESC"
        //     " GROUP BY h.historyId, h.hu_userId, h.hb_bookId, h.historyCreateDateTime, h.historyUpdateDateTime" 
            , nativeQuery = true)
    Page<History> getBookHistory(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = "SELECT COUNT(*) > 0 FROM History WHERE hu_userId = :userId AND hb_bookId = :bookId", nativeQuery = true)
    Integer existsByUserIdAndBookId(Integer userId, Integer bookId);    

    @Modifying
    @Transactional
    @Query(value = "UPDATE History SET historyUpdateDateTime = NOW() WHERE hu_userId = :userId AND hb_bookId = :bookId", nativeQuery = true)
    Integer updateHistory(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM History WHERE historyId = :historyId", nativeQuery = true)
    Integer deleteHistory(@Param("historyId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM History WHERE hu_userId = :userId", nativeQuery = true)
    Integer deleteHistoryByUserId(@Param("userId") Integer userId);

    boolean existsByHistoryId(Integer historyId);
}
