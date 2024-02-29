package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.CheckLikeReview;

public interface CheckLikeReviewRepository extends JpaRepository<CheckLikeReview, Integer> {

    @Query(value = "SELECT cr.checkLikeReviewId, cr.clr_reviewId, cr.clu_userId, likeStatus, cr.checkLikeReviewCreateDateTime, cr.checkLikeReviewUpdateDateTime " +
                    " FROM CheckLikeReview cr WHERE clu_userId = :clu_userId", nativeQuery = true)
    List<CheckLikeReview> getLikeStatus(@Param("clu_userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CheckLikeReview (clr_reviewId, clu_userId, likeStatus)" +
            "VALUES (:clr_reviewId, :clu_userId, :likeStatus);", nativeQuery = true)
    void insertCheckLikeReview(@Param("clr_reviewId") Integer reviewId,
            @Param("clu_userId") Integer userId,
            @Param("likeStatus") Integer likeStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CheckLikeReview SET likeStatus = :likeStatus WHERE checkLikeReviewId = :checkLikeReviewId", nativeQuery = true)
    void updateCheckLikeReview(@Param("likeStatus") Integer likeStatus,
            @Param("checkLikeReviewId") Integer checkLikeReviewId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CheckLikeReview WHERE checkLikeReviewId = :checkLikeReviewId", nativeQuery = true)
    void deleteCheckLikeReview(@Param("checkLikeReviewId") Integer checkLikeReviewId);

}
