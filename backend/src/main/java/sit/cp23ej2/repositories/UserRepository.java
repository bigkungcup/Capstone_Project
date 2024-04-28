package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import sit.cp23ej2.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.followings, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio "
                        +
                        "FROM User u" +
                        " WHERE (:role IS NULL OR u.role = :role) " +
                        " AND (:search IS NULL OR u.displayName LIKE %:search% OR u.email LIKE %:search%)", nativeQuery = true)
        Page<User> getAllUsers(Pageable pageable, @Param("role") String role, @Param("search") String search);

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.followings, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u", nativeQuery = true)
        List<User> getAllUserList();

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.followings, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio "
                        +
                        " FROM User u " +
                        " WHERE u.role = 'USER' AND u.followers != 0 AND u.totalLike != 0" +
                        " ORDER BY CASE WHEN :sort_param = 'followers' THEN u.followers END DESC, " +
                        " CASE WHEN :sort_param = 'totalLike' THEN u.totalLike END DESC " +
                        " LIMIT 5", nativeQuery = true)
        List<User> getUserRanking(@Param("sort_param") String sort_param);

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.followings, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u WHERE userId = :userId", nativeQuery = true)
        User getUserById(@Param("userId") int userId);

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.followings, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u WHERE email = :email", nativeQuery = true)
        User getUserByEmail(@Param("email") String email);

        boolean existsByEmailOrDisplayName(String email, String displayName);

        boolean existsByEmail(String email);

        boolean existsByDisplayName(String displayName);

        boolean existsByUserId(Integer userId);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO  User (displayName, email, password, role, followers, followings, totalReview, totalFavoriteReview, totalLike, bio)"
                        +
                        "values (:displayName, :email, :password, :role, 0, 0, 0, 0, 0, :bio);", nativeQuery = true)
        Integer insertUser(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("password") String password, @Param("role") String role, @Param("bio") String bio);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET displayName = :displayName, email = :email, bio = :bio" +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUser(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("bio") String bio, @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET displayName = :displayName, email = :email, password = :password, bio = :bio"
                        +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserByAdmin(@Param("displayName") String displayName,
                        @Param("password") String password, @Param("bio") String bio,
                        @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET displayName = :displayName, email = :email, bio = :bio"
                        +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserNoPasswordByAdmin(@Param("displayName") String displayName,
                        @Param("bio") String bio, @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET bio = :bio" +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserDetailBio(@Param("bio") String bio, @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET password = :password, bio = :bio"
                        +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserDetailByAdmin(@Param("password") String password, @Param("bio") String bio,
                        @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET password = :password" +
                        " WHERE email = :email", nativeQuery = true)
        void resetPassword(@Param("email") String email,
                        @Param("password") String password);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET totalReview = totalReview + 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void increaseTotalReview(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET totalReview = totalReview - 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void decreaseTotalReview(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET followings = followings + 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void increaseFollowings(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET followings = followings - 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void decreaseFollowings(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET followers = followers + 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void increaseFollowers(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET followers = followers - 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void decreaseFollowers(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM User WHERE userId = :userId", nativeQuery = true)
        Integer deleteUser(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET totalLike = totalLike + 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void increaseTotalLike(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET totalLike = totalLike - 1" +
                        " WHERE userId = :userId", nativeQuery = true)
        void decreaseTotalLike(@Param("userId") Integer userId);

}
