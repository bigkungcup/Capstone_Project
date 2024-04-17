package sit.cp23ej2.dtos.Report;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReportDTO {
    private Integer reportId; 

    private String reportTitle;

    private String reportDetail;

    private Integer problemId;

    private String reportType;

    private Integer reportStatus;

    private LocalDateTime reportCreateDateTime;

    private LocalDateTime reportUpdateDateTime;

    private User reportBy;
    
    private User fixBy;

    private Object data;

    @JsonIgnore
    private Book book;

    @JsonIgnore
    private Review review;

    @JsonIgnore
    private User user;


}
