package sit.cp23ej2.controllers;

import java.util.List;

import org.springframework.data.domain.Page;

import sit.cp23ej2.dtos.Paginate;

public class CommonController {

    public Paginate paginate(int page, int size, Page<?> data) {
        Paginate paginate = new Paginate();
        paginate.setPageNo(page);
        paginate.setPageSize(size);
        paginate.setTotalElements(data.getTotalElements());
        paginate.setTotalPages((int) Math.ceil((double) data.getTotalElements() / size));
        paginate.setLast(page == (int) Math.ceil((double) data.getTotalElements() / size) ? true : false);
        return paginate;
    }
}
