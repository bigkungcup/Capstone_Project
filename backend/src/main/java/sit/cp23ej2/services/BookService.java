package sit.cp23ej2.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.Paginate;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;

@Service
public class BookService extends CommonController {

    @Autowired
    private BookRepository repository;

    public DataResponse getBook(int page, int limit) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        List<Book> book = repository.getAllBooks();
        if(book != null){
            Paginate pagination = this.paginate(page, limit, book);
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All books");
            response.setResponse_datetime(Instant.now());
            response.setData(book);
            response.setPaginate(pagination);
        }else{
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
        
        return response;
    }

    public DataResponse getBookById(int bookId) throws HandleExceptionNotFound{
        DataResponse response = new DataResponse();
        Book book = repository.findBookById(bookId);

        if (book != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Book Detail");
            response.setResponse_datetime(Instant.now());
            response.setData(book);
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
        return response;
    }
}
