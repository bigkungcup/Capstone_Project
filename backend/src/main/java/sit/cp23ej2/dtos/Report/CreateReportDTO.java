package sit.cp23ej2.dtos.Report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateReportDTO {
    
    @NotBlank(message = "Report Title shouldn't be blank or null")
    private String reportTitle;
    
    @NotBlank(message = "Report Detail shouldn't be blank or null")
    private String reportDetail;

    @NotNull(message = "Problem shouldn't be null")
    private Integer problemId;

    @NotBlank(message = "Report Type shouldn't be blank or null")
    private String reportType;
}
