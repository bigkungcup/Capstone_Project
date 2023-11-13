package sit.cp23ej2.controllers;

import java.util.List;

import sit.cp23ej2.dtos.Paginate;

public class CommonController {

    public Paginate paginate(int page, int limit, List<?> data) {
        Paginate paginate = new Paginate();
        paginate.setPageNo(page);
        paginate.setPageSize(limit);
        paginate.setTotalElements(data.size());
        paginate.setTotalPages((int) Math.ceil((double) data.size() / limit));
        paginate.setLast(page == (int) Math.ceil((double) data.size() / limit) ? true : false);
        return paginate;
    }
}
