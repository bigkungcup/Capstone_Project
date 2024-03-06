package sit.cp23ej2.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Following", schema = "bannarug")
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followingId")
    private Integer followingId; 

    // private Integer fu_userId;

    // private Integer userfollowingId;

    private Integer followingStatus;

    private LocalDateTime followingCreateDateTime;

    private LocalDateTime followingUpdateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fu_userId", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userFollowingId", nullable = true)
    private User userFollowing;
}
