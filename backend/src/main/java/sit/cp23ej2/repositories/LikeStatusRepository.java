package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.LikeStatus;

public interface LikeStatusRepository extends JpaRepository<LikeStatus, Integer> {

    @Query(value = "SELECT ls.likeStatusId, ls.lsr_reviewId, ls.lsu_userId, ls.likeStatus, ls.likeStatusCreateDateTime, ls.likeStatusUpdateDateTime " +
                    " FROM LikeStatus ls WHERE ls.lsu_userId = :lsu_userId", nativeQuery = true)
    List<LikeStatus> getLikeStatus(@Param("lsu_userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO LikeStatus (lsr_reviewId, lsu_userId, likeStatus)" +
            "VALUES (:lsr_reviewId, :lsu_userId, :likeStatus);", nativeQuery = true)
    void insertLikeStatus(@Param("lsr_reviewId") Integer reviewId,
            @Param("lsu_userId") Integer userId,
            @Param("likeStatus") Integer likeStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE LikeStatus SET likeStatus = :likeStatus WHERE likeStatusId = :likeStatusId", nativeQuery = true)
    void updateLikeStatus(@Param("likeStatus") Integer likeStatus,
            @Param("likeStatusId") Integer likeStatusId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM LikeStatus WHERE likeStatusId = :likeStatusId", nativeQuery = true)
    void deleteLikeStatus(@Param("likeStatusId") Integer likeStatusId);

    boolean existsByLikeStatusId(Integer likeStatusId);

}
