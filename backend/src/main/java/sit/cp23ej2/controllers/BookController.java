package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.CreateBookDTO;
import sit.cp23ej2.dtos.Book.UpdateBookDTO;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.BookService;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "*"
}, methods = {
        RequestMethod.OPTIONS,
        RequestMethod.GET,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@Validated
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("")
    public DataResponse getAllBook(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(required =  false) Long  bookRating, @RequestParam(required =  false) String  sortBy, @RequestParam(required =  false) String sortType) throws HandleExceptionNotFound {
        return bookService.getBook(page, size, bookRating, sortBy, sortType);

    }

    @RequestMapping("/guest")
    public DataResponse getAllBookByGuest(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(required =  false) Long  bookRating, @RequestParam(required =  false) String  sortBy, @RequestParam(required =  false) String sortType) throws HandleExceptionNotFound {
        return bookService.getBook(page, size, bookRating, sortBy, sortType);

    }

    @RequestMapping("/{bookId}")
    public DataResponse getBookById(@PathVariable Integer bookId) throws HandleExceptionNotFound {
        return bookService.getBookById(bookId);
    }

    @RequestMapping("/guest/{bookId}")
    public DataResponse getBookByIdGuest(@PathVariable Integer bookId) throws HandleExceptionNotFound {
        return bookService.getBookById(bookId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse createBook(@RequestPart("book") @Valid CreateBookDTO book, @RequestPart(value = "file") @Nullable MultipartFile file) {
        return bookService.createBook(book,file);
    }

    @PutMapping("/{bookId}")
    public DataResponse updateBook(@RequestPart("book") @Valid  UpdateBookDTO book, @PathVariable Integer bookId, @RequestPart(required = false, value = "file") MultipartFile file) {
        return bookService.updateBook(book, bookId, file);
    }

    @DeleteMapping("/{bookId}")
    public DataResponse deleteBook(@PathVariable Integer bookId) {
        return bookService.deleteBook(bookId);
    }
}
