package sit.cp23ej2.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "LikeStatus", schema = "bannarug")
public class LikeStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeStatusId")
    private Integer likeStatusId;

    private Integer lsr_reviewId; 
    
    private Integer lsu_userId; 

    private Integer likeStatus;

    private LocalDateTime likeStatusCreateDateTime;

    private LocalDateTime likeStatusUpdateDateTime;
}
