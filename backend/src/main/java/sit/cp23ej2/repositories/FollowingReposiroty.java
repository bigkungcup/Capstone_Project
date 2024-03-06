package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Following;

public interface FollowingReposiroty extends JpaRepository<Following, Integer> {

    @Query(value = "SELECT f.followingId, f.fu_userId, f.userFollowingId, f.followingStatus, f.followingCreateDateTime, f.followingUpdateDateTime "
            +
            " FROM following f " +
            " WHERE f.fu_userId = :userId", nativeQuery = true)
    Page<Following> getFollowings(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = "SELECT f.followingId, f.fu_userId, f.userFollowingId, f.followingStatus, f.followingCreateDateTime, f.followingUpdateDateTime "
            +
            " FROM following f " +
            " WHERE f.userFollowingId = :userId", nativeQuery = true)
    Page<Following> getFollowers(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = "SELECT f.followingId, f.fu_userId, f.userFollowingId, f.followingStatus, f.followingCreateDateTime, f.followingUpdateDateTime "
            +
            " FROM following f " +
            " WHERE f.fu_userId = :userId", nativeQuery = true)
    List<Following> getReviewFollowings(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO following (fu_userId, userFollowingId, followingStatus)" +
            "values (:fu_userId, :userFollowingId, :followingStatus);", nativeQuery = true)
    void insertfollowing(@Param("fu_userId") Integer fu_userId, @Param("userFollowingId") Integer userFollowingId,
            @Param("followingStatus") Integer followingStatus);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM following WHERE fu_userId = :fu_userId AND userFollowingId = :userFollowingId", nativeQuery = true)
    void deletefollowing(@Param("fu_userId") Integer fu_userId, @Param("userFollowingId") Integer userFollowingId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM following WHERE fu_userId = :fu_userId AND userFollowingId = :userFollowingId", nativeQuery = true)
    Long checkExists(Integer fu_userId, Integer userFollowingId);
}
