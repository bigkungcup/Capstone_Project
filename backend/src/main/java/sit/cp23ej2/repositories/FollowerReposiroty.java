package sit.cp23ej2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Follower;

public interface FollowerReposiroty extends JpaRepository<Follower, Integer>{
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  Follower (fu_userId, userFollowerId)" +
            "values (:fu_userId, :userFollowerId);", nativeQuery = true)
    void insertFollower(@Param("fu_userId") Integer fu_userId, @Param("userFollowerId") Integer userFollowerId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Follower WHERE fu_userId = :fu_userId AND userFollowerId = :userFollowerId", nativeQuery = true)
    void deleteFollower(@Param("fu_userId") Integer fu_userId, @Param("userFollowerId") Integer userFollowerId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " + 
                  "FROM Follower WHERE fu_userId = :fu_userId AND userFollowerId = :userFollowerId", nativeQuery = true)
    Long checkExists(Integer fu_userId, Integer userFollowerId);
}
