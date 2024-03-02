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
@Table(name = "BookmarkStatus", schema = "bannarug")
public class BookmarkStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarkStatusId")
    private Integer bookmarkStatusId;

    private Integer bsb_bookmarkId;

    private Integer bsu_userId;

    private LocalDateTime bookmarkStatusCreateDateTime;

    private LocalDateTime bookmarkStatusUpdateDateTime;
}
