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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Report", schema = "bannarug")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportId")
    private Integer reportId; 

    private String reportTitle;

    private String reportDetail;

    private Integer problemId;

    private String reportType;

    private Integer reportStatus;

    private LocalDateTime reportCreateDateTime;

    private LocalDateTime reportUpdateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportBy", nullable = true)
    private User reportBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixBy", nullable = true)
    private User fixBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rr_reviewId", nullable = true)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rb_bookId", nullable = true)
    private Book book;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ru_userId", nullable = true)
    private User user;
}
