package sit.cp23ej2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import sit.cp23ej2.entities.Recommend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {

    @Query(value = "SELECT r.rb_booktypeId FROM Recommend r " +
            "WHERE r.ru_userId = :userId AND r.viewCount = (SELECT MAX(rec.viewCount) FROM Recommend rec WHERE rec.ru_userId = :userId) limit 1", nativeQuery = true)
    Integer getRecommendBooktypeIdByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM Recommend WHERE rb_booktypeId = :booktypeId AND ru_userId = :userId", nativeQuery = true)
    Recommend existsByBooktypeIdAndUserId(@Param("booktypeId") Integer booktypeId, @Param("userId")  Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Recommend (rb_booktypeId, ru_userId, viewCount) " +
            "VALUES (:booktypeId, :userId, 1)", nativeQuery = true)
    void insertRecommend(@Param("booktypeId") Integer booktypeId, @Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Recommend SET viewCount = viewCount + 1 " +
            "WHERE rb_booktypeId = :booktypeId AND ru_userId = :userId ", nativeQuery = true)
    void updateRecommend(@Param("booktypeId") Integer booktypeId, @Param("userId") Integer userId);

}