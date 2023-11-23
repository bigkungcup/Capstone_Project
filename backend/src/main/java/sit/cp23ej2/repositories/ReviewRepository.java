package sit.cp23ej2.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sit.cp23ej2.entities.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

        @Query(value = "SELECT * FROM review WHERE rvb_bookId = :bookId", nativeQuery = true)
        Page<Review> getReviewByBookId(@Param("bookId") int bookId, Pageable pageable);

        @Query(value = "SELECT * FROM review WHERE reviewId = :reviewId", nativeQuery = true)
        Review getReviewById(@Param("reviewId") int reviewId);
        
        @Modifying
        @Transactional
        @Query(value = "INSERT INTO review (reviewRating, reviewDetail, reviewTitle, reviewTotalLike, rvu_userId, rvb_bookId, spoileFlag)" +
                        "VALUES (:rating, :detail, :title, 0, :userId, :bookId, 0 );", nativeQuery = true)
        void insertReview(@Param("rating") Long reviewRating, @Param("detail") String reviewDetail,
                        @Param("title") String reviewTitle,
                        @Param("userId") Integer rvu_userId,
                        @Param("bookId") Integer rvb_bookId);
        @Modifying
        @Transactional
        @Query(value = "UPDATE review SET reviewTitle = :title, reviewDetail= :detail, reviewRating = :rating, spoileFlag = :spoileFlag" +
                        " WHERE reviewId = :reviewId;", nativeQuery = true)
        void updateReview(@Param("rating") Long reviewRating, @Param("detail") String reviewDetail,
                        @Param("title") String reviewTitle, @Param("spoileFlag") Integer spoileFlag,
                        @Param("reviewId") Integer reviewId);

        @Query(value = "DELETE FROM review WHERE reviewId = :reviewId", nativeQuery = true)
        void deleteReview(@Param("reviewId") Integer reviewId);

        List<Review> findAll();
}
