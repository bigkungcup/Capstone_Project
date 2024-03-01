package sit.cp23ej2.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

// import java.util.List;

import org.springframework.data.domain.Page;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Paginate;

public class CommonController {

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Paginate paginate(int page, int size, Page<?> data) {
        Paginate paginate = new Paginate();
        paginate.setPageNo(page);
        paginate.setPageSize(size);
        paginate.setTotalElements(data.getTotalElements());
        paginate.setTotalPages((int) Math.ceil((double) data.getTotalElements() / size));
        paginate.setLast(page == (int) Math.ceil((double) data.getTotalElements() / size) ? true : false);
        return paginate;
    }

    public DataResponse responseWithData(Object data, Integer code, String status, String message) {
        DataResponse response = new DataResponse();
        response.setResponse_code(code);
        response.setResponse_status(status);
        response.setResponse_message(message);
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setData(data);

        return response;
    }

    public DataResponse response(Integer code, String status, String message) {
        DataResponse response = new DataResponse();
        response.setResponse_code(code);
        response.setResponse_status(status);
        response.setResponse_message(message);
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
    
        return response;
    }
}
