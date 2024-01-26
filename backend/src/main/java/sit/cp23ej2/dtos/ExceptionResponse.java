package sit.cp23ej2.dtos;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ExceptionResponse {
    private Integer response_code;
    private String response_status;
    private String response_message;
    private String response_datetime;
    private String path;
    private Map<String,String> filedErrors;
}
