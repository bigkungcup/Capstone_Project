package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.cp23ej2.entities.BookmarkStatus;

public interface BookmarkStatusRepository extends JpaRepository<BookmarkStatus, Integer> {

    @Query(value = "SELECT bs.bookmarkStatusId, bs.bsb_bookmarkId, bs.bsu_userId, bs.bookmarkStatusCreateDateTime, bs.bookmarkStatusUpdateDateTime " +
            " FROM BookmarkStatus bs WHERE bs.bsu_userId = :userId ", nativeQuery = true)
    List<BookmarkStatus> getBookmarkStatus(@Param("userId") Integer userId);
}
