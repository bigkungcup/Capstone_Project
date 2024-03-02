package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.BookmarkStatus;

public interface BookmarkStatusRepository extends JpaRepository<BookmarkStatus, Integer> {

    @Query(value = "SELECT bs.bookmarkStatusId, bs.bsb_bookmarkId, bs.bsu_userId, bs.bookmarkStatus, bs.bookmarkStatusCreateDateTime, bs.bookmarkStatusUpdateDateTime " +
            " FROM BookmarkStatus bs WHERE bs.bsu_userId = :userId ", nativeQuery = true)
    List<BookmarkStatus> getBookmarkStatus(@Param("userId") Integer userId);

    @Query(value = "SELECT bs.bookmarkStatusId, bs.bsb_bookmarkId, bs.bsu_userId, bs.bookmarkStatus, bs.bookmarkStatusCreateDateTime, bs.bookmarkStatusUpdateDateTime " +
            " FROM BookmarkStatus bs WHERE bs.bsb_bookmarkId = :bookmarkId ", nativeQuery = true)
    List<BookmarkStatus> getBookmarkStatusByBookmarkId(@Param("bookmarkId") Integer bookmarkId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO BookmarkStatus (bsb_bookmarkId, bsu_userId, bookmarkStatus)" +
            "VALUES (:bsb_bookmarkId, :bsu_userId, :bookmarkStatus);", nativeQuery = true)
    void insertBookmarkStatus(@Param("bsb_bookmarkId") Integer bookmarkId,
            @Param("bsu_userId") Integer userId,
            @Param("bookmarkStatus") Integer bookmarkStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE BookmarkStatus SET bookmarkStatus = :bookmarkStatus WHERE bookmarkStatusId = :bookmarkStatusId", nativeQuery = true)
    void updateBookmarkStatus(@Param("bookmarkStatus") Integer bookmarkStatus,
            @Param("bookmarkStatusId") Integer bookmarkStatusId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BookmarkStatus WHERE bookmarkStatusId = :bookmarkStatusId", nativeQuery = true)
    void deleteBookmarkStatus(@Param("bookmarkStatusId") Integer bookmarkStatusId);
}
