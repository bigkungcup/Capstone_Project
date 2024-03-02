package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.FollowerStatus;

public interface FollowerStatusRepository extends JpaRepository<FollowerStatus, Integer>{

    @Query(value = "SELECT fs.followerStatusId, fs.fsu_userFollowerId, fs.fsu_userId, fs.followerStatus, fs.followerStatusCreateDateTime, fs.followerStatusUpdateDateTime " +
                    " FROM FollowerStatus fs WHERE fs.fsu_userId = :fsu_userId", nativeQuery = true)
    List<FollowerStatus> getFollowerStatus(@Param("fsu_userId") Integer userId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO FollowerStatus (fsu_userFollowerId, fsu_userId, followerStatus)" +
            "VALUES (:fsu_userFollowerId, :fsu_userId, :followerStatus);", nativeQuery = true)
    void insertFollowerStatus(@Param("fsu_userFollowerId") Integer followerId,
            @Param("fsu_userId") Integer userId,
            @Param("followerStatus") Integer followerStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE FollowerStatus SET followerStatus = :followerStatus WHERE followerStatusId = :followerStatusId", nativeQuery = true)
    void updateFollowerStatus(@Param("followerStatus") Integer FollowerStatus,
            @Param("followerStatusId") Integer followerStatusId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM FollowerStatus WHERE followerStatusId = :followerStatusId", nativeQuery = true)
    void deleteFollowerStatus(@Param("followerStatusId") Integer followerStatusId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " + 
                  "FROM FollowerStatus WHERE fsu_userFollowerId = :userFollowerId AND fsu_userId = :fsu_userId", nativeQuery = true)
    Long checkExistsByUserFollowerId(@Param("userFollowerId") Integer userFollowerId, @Param("fsu_userId") Integer userId);

    boolean existsByFollowerStatusId(Integer followerStatusId);

}
