package sit.cp23ej2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Bookmark;


public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    @Query(value = "SELECT b.bookmarkId, b.bb_bookId, b.bu_userId, b.bookmarkCreateDateTime, b.bookmarkUpdateDateTime FROM Bookmark b WHERE b.bu_userId = :userId", nativeQuery = true)
    Page<Bookmark> getBookmarkByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Bookmark (bb_bookId, bu_userId)" +
            " VALUES (:bb_bookId, :bu_userId );", nativeQuery = true)
    void insertBookmark(@Param("bb_bookId") Integer bb_bookId, @Param("bu_userId") Integer bu_userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Bookmark WHERE bb_bookId = :bb_bookId AND bu_userId = :bu_userId", nativeQuery = true)
    void deleteBookmark(@Param("bb_bookId") Integer bb_bookId, @Param("bu_userId") Integer bu_userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Bookmark WHERE bookmarkId = :bookmarkId", nativeQuery = true)
    void deleteBookmarkById(@Param("bookmarkId") Integer bb_bookId);

    boolean existsByBookmarkId(Integer bookmarkId);
}
