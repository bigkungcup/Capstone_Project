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
@Table(name = "FollowerStatus", schema = "bannarug")
public class FollowerStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followerStatusId")
    private Integer followerStatusId;

    private Integer fsu_userId;

    private Integer fsu_userFollowerId;

    private Integer followerStatus;

    private LocalDateTime followerStatusCreateDateTime;

    private LocalDateTime followerStatusUpdateDateTime;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "fsu_userId", nullable = true)
    // private User user;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "fsu_userFollowerId", nullable = true)
    // private User userFollower;
}
