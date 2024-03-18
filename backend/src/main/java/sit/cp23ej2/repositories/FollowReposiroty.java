package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Follow;

public interface FollowReposiroty extends JpaRepository<Follow, Integer> {

    @Query(value = "SELECT f.followId, f.fu_userId, f.userFollowId, f.followStatus, f.followCreateDateTime, f.followUpdateDateTime "
            +
            " FROM Follow f " +
            " WHERE f.fu_userId = :userId", nativeQuery = true)
    Page<Follow> getFollowings(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = "SELECT f.followId, f.fu_userId, f.userFollowId, f.followStatus, f.followCreateDateTime, f.followUpdateDateTime "
            +
            " FROM Follow f " +
            " WHERE f.fu_userId = :userId", nativeQuery = true)
    List<Follow> getFollowingList(@Param("userId") Integer userId);

    @Query(value = "SELECT f.followId, f.fu_userId, f.userFollowId, f.followStatus, f.followCreateDateTime, f.followUpdateDateTime "
            +
            " FROM Follow f " +
            " WHERE f.userFollowId = :userId", nativeQuery = true)
    Page<Follow> getFollowers(Pageable pageable, @Param("userId") Integer userId);

    @Query(value = "SELECT f.followId, f.fu_userId, f.userFollowId, f.followStatus, f.followCreateDateTime, f.followUpdateDateTime "
            +
            " FROM Follow f " +
            " WHERE f.fu_userId = :userId", nativeQuery = true)
    List<Follow> getReviewFollowings(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Follow (fu_userId, userFollowId, followStatus)" +
            "values (:fu_userId, :userFollowId, :followStatus);", nativeQuery = true)
    void insertfollowing(@Param("fu_userId") Integer fu_userId, @Param("userFollowId") Integer userFollowId,
            @Param("followStatus") Integer followStatus);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Follow WHERE fu_userId = :fu_userId AND userFollowId = :userFollowId", nativeQuery = true)
    void deletefollowing(@Param("fu_userId") Integer fu_userId, @Param("userFollowId") Integer userFollowId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM Follow WHERE fu_userId = :fu_userId AND userFollowId = :userFollowId", nativeQuery = true)
    Long checkExists(Integer fu_userId, Integer userFollowId);
}
