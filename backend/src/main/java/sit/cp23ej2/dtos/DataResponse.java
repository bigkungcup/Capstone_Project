package sit.cp23ej2.dtos;


import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse {
    private Integer response_code;
    private String response_status;
    private String response_message;
    private Instant response_datetime;
    private Object data;
    // private Paginate paginate;
}
