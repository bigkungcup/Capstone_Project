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

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  User (displayName, email, password, role, followers, follows, totalReview, totalFavoriteReview, totalLike, bio)" +
                        "values (:displayName, :email, :password, :role, 0, 0, 0, 0, 0, :bio);", nativeQuery = true)
        Integer insertUser(@Param("displayName") String displayName, @Param("email") String email,
                        @Param("password") String password, @Param("role") String role, @Param("bio") String bio);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM User WHERE userId = :userId", nativeQuery = true)
    Integer deleteUser(@Param("userId") Integer userId);

}
