package sit.cp23ej2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.cp23ej2.entities.Review;


public interface ReviewRepository extends JpaRepository<Review, Integer>{
    
}
