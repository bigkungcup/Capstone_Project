package sit.cp23ej2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import sit.cp23ej2.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.follows, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u", nativeQuery = true)
        Page<User> getAllUsers(Pageable pageable);

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.follows, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u WHERE userId = :userId", nativeQuery = true)
        User getUserById(@Param("userId") int userId);

        @Query(value = "SELECT u.userId, u.displayName, u.email, u.password, u.role, u.followers, u.follows, u.totalReview, u.totalFavoriteReview, u.totalLike, u.bio FROM User u WHERE email = :email", nativeQuery = true)
        User getUserByEmail(@Param("email") String email);

        boolean existsByEmailOrDisplayName(String email, String displayName);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO  User (displayName, email, password, role, followers, follows, totalReview, totalFavoriteReview, totalLike, bio)"
                        +
                        "values (:displayName, :email, :password, :role, 0, 0, 0, 0, 0, :bio);", nativeQuery = true)
        Integer insertUser(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("password") String password, @Param("role") String role, @Param("bio") String bio);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET displayName = :displayName, email = :email, password = :password, bio = :bio" +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUser(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("password") String password, @Param("bio") String bio, @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET displayName = :displayName, email = :email, password = :password, bio = :bio, role = :role"
                        +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserByAdmin(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("password") String password, @Param("bio") String bio, @Param("role") String role,
                        @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET password = :password, bio = :bio" +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserDetailByUser(@Param("password") String password, @Param("bio") String bio, @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET password = :password, bio = :bio, role = :role"
                        +
                        " WHERE userId = :userId", nativeQuery = true)
        void updateUserDetailByAdmin(@Param("password") String password, @Param("bio") String bio,
                        @Param("role") String role,
                        @Param("userId") int userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE User SET password = :password" +
                        " WHERE email = :email", nativeQuery = true)
        void updatePassword(@Param("email") String email,
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
        @Query(value = "DELETE FROM User WHERE userId = :userId", nativeQuery = true)
        Integer deleteUser(@Param("userId") Integer userId);

}
