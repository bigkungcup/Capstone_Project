package sit.cp23ej2.services;

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
        // DataResponse response = new DataResponse();
        List<Booktype> allBooktype = booktypeRepository.getAllBooktype();
        return responseWithData(allBooktype, 200, "OK", "All Booktype");
    }

    public DataResponse insertBooktype(CreateBooktypeDTO booktype){
        // DataResponse response = new DataResponse();
        booktypeRepository.insertBooktype(booktype.getBooktypeName());
        return response(201, "Created", "Booktype Created");
    }

    public DataResponse deleteBooktype(Integer booktypeId){
        // DataResponse response = new DataResponse();
        if(!booktypeRepository.existsByBooktypeId(booktypeId)){
           throw new HandleExceptionNotFound("Booktype Not Found", "Booktype");
        }
        booktypeRepository.deleteBooktype(booktypeId);
        return response(200, "OK", "Booktype Deleted");
    }
}
