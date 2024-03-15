package sit.cp23ej2.dtos.Report;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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


}
