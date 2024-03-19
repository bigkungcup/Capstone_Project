package sit.cp23ej2.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sit.cp23ej2.dtos.Review.ReviewRatingStatisticsDTO;
import sit.cp23ej2.entities.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

        @Query(value = "SELECT r.reviewId, r.rvu_userId, r.rvb_bookId, r.reviewTitle, r.reviewDetail, r.reviewRating, r.spoileFlag, r.reviewTotalLike, r.reviewTotalDisLike, r.reviewCreateDateTime, r.reviewUpdateDateTime"
                        +
                        " FROM Review r " +
                        " WHERE r.rvb_bookId = :bookId AND (:reviewRating IS NULL OR r.reviewRating = :reviewRating)", nativeQuery = true)
        Page<Review> getReviewByBookId(Pageable pageable, @Param("bookId") Integer bookId,
                        @Param("reviewRating") Long reviewRating);

        @Query(value = "SELECT r.reviewId, r.rvu_userId, r.rvb_bookId, r.reviewTitle, r.reviewDetail, r.reviewRating, r.spoileFlag, r.reviewTotalLike, r.reviewTotalDisLike, r.reviewCreateDateTime, r.reviewUpdateDateTime "
                        +
                        " FROM Review r " +
                        " WHERE r.reviewId = :reviewId", nativeQuery = true)
        Review getReviewById(@Param("reviewId") Integer reviewId);

        @Query(value = "SELECT r.reviewId, r.rvu_userId, r.rvb_bookId, r.reviewTitle, r.reviewDetail, r.reviewRating, r.spoileFlag, r.reviewTotalLike, r.reviewTotalDisLike, r.reviewCreateDateTime, r.reviewUpdateDateTime "
                        +
                        " FROM Review r WHERE r.rvu_userId = :userId " + 
                        " ORDER BY r.reviewCreateDateTime DESC "
                        , nativeQuery = true)
        Page<Review> getReviewByUserId(Pageable pageable, @Param("userId") Integer userId);

        @Query(value = "SELECT r.reviewId, r.rvu_userId, r.rvb_bookId, r.reviewTitle, r.reviewDetail, r.reviewRating, r.spoileFlag, r.reviewTotalLike, r.reviewTotalDisLike, r.reviewCreateDateTime, r.reviewUpdateDateTime "
                        +
                        " FROM Review r " +
                        " ORDER BY r.reviewCreateDateTime DESC LIMIT 10", nativeQuery = true)
        List<Review> getReviewByCreateDateTime();

        @Query(value = "SELECT (SELECT COUNT(reviewId) FROM Review) as all_star, (SELECT COUNT(reviewId) FROM Review WHERE reviewRating = 5) as 5_star, (SELECT COUNT(reviewId) FROM Review WHERE reviewRating = 4)  as 4_star, "
                        +
                        " (SELECT COUNT(reviewId) FROM Review WHERE reviewRating = 3)  as 3_star, (SELECT COUNT(reviewId) FROM Review WHERE reviewRating = 2) as 2_star, "
                        +
                        " (SELECT COUNT(reviewId) FROM Review WHERE reviewRating = 1) as 1_star", nativeQuery = true)
        ReviewRatingStatisticsDTO getReviewRatingCount();

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO Review (reviewRating, reviewDetail, reviewTitle, reviewTotalLike, reviewTotalDisLike, rvu_userId, rvb_bookId, spoileFlag)"
                        +
                        "VALUES (:rating, :detail, :title, 0, 0, :userId, :bookId, :spoileFlag );", nativeQuery = true)
        void insertReview(@Param("rating") Long reviewRating, @Param("detail") String reviewDetail,
                        @Param("title") String reviewTitle,
                        @Param("userId") Integer rvu_userId,
                        @Param("bookId") Integer rvb_bookId,
                        @Param("spoileFlag") Integer spoileFlag);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewRating = :rating, reviewDetail= :detail, reviewTitle = :title, spoileFlag = :spoileFlag"
                        +
                        " WHERE reviewId = :reviewId", nativeQuery = true)
        void updateReview(@Param("rating") Long reviewRating, @Param("detail") String reviewDetail,
                        @Param("title") String reviewTitle,
                        @Param("spoileFlag") Integer spoileFlag,
                        @Param("reviewId") Integer reviewId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewTotalLike = :reviewTotalLike, reviewTotalDisLike= :reviewTotalDisLike" +
                        " WHERE reviewId = :reviewId", nativeQuery = true)
        void updateTotalLikeAndTotalDisLike(@Param("reviewTotalLike") Integer reviewTotalLike,
                        @Param("reviewTotalDisLike") Integer reviewTotalDisLike,
                        @Param("reviewId") Integer reviewId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM Review WHERE reviewId = :reviewId", nativeQuery = true)
        Integer deleteReview(@Param("reviewId") Integer reviewId);

        List<Review> findAll();

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewTotalLike = reviewTotalLike + 1  WHERE reviewId = :reviewId", nativeQuery = true)
        void increaseReviewTotalLike(@Param("reviewId") Integer reviewId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewTotalLike = reviewTotalLike - 1  WHERE reviewId = :reviewId", nativeQuery = true)
        void decreaseReviewTotalLike(@Param("reviewId") Integer reviewId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewTotalDisLike = reviewTotalDisLike + 1  WHERE reviewId = :reviewId", nativeQuery = true)
        void increaseReviewTotalDisLike(@Param("reviewId") Integer reviewId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Review SET reviewTotalDisLike = reviewTotalDisLike - 1  WHERE reviewId = :reviewId", nativeQuery = true)
        void decreaseReviewTotalDisLike(@Param("reviewId") Integer reviewId);
}
