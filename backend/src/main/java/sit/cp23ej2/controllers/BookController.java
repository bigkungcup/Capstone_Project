package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.BookService;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("")
    public DataResponse getAllBook(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) throws HandleExceptionNotFound{
       return bookService.getBook(page, limit);

    }

    @RequestMapping("/{bookId}")
    public DataResponse getBookById(@PathVariable Integer bookId) throws HandleExceptionNotFound {
        return bookService.getBookById(bookId);
    }

}
