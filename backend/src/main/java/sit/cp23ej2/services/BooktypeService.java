package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Booktype.CreateBooktypeDTO;
import sit.cp23ej2.entities.Booktype;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BooktypeRepository;

@Service
public class BooktypeService extends CommonController{
    
    @Autowired
    private BooktypeRepository booktypeRepository;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getAllBooktype(){
        DataResponse response = new DataResponse();
        List<Booktype> allBooktype = booktypeRepository.getAllBooktype();;
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Get All Booktype");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setData(allBooktype);
        return response;
    }

    public DataResponse insertBooktype(CreateBooktypeDTO booktype){
        DataResponse response = new DataResponse();
        booktypeRepository.insertBooktype(booktype.getBookTypeName());
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Booktype Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse deleteBooktype(Integer booktypeId){
        DataResponse response = new DataResponse();
        if(!booktypeRepository.existsByBooktypeId(booktypeId)){
           throw new HandleExceptionNotFound("Booktype Not Found", "Booktype");
        }
        booktypeRepository.deleteBooktype(booktypeId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Booktype Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
