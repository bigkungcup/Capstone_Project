package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Booktype;

public interface BooktypeRepository extends JpaRepository<Booktype, Integer>{

    @Query(value = "SELECT * FROM Booktype ", nativeQuery = true)
    List<Booktype> getAllBooktype();

    @Query(value = "SELECT * FROM Booktype WHERE bookTypeId = :bookTypeId", nativeQuery = true)
    Booktype getBooktypeById(@Param("bookTypeId") Integer bookTypeId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  Booktype (bookTypeName)" +
            "values (:bookTypeName);", nativeQuery = true)
    Integer insertBooktype(@Param("bookTypeName") String bookTypeName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Booktype WHERE booktypeId = :booktypeId", nativeQuery = true)
    Integer deleteBooktype(@Param("booktypeId") Integer booktypeId);

    boolean existsByBooktypeId(Integer bookTypeId);

}
