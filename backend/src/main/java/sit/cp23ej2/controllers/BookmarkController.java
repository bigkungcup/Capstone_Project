package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.BookmarkService;

@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://capstone23.sit.kmutt.ac.th/ej2"
}, methods = {
    RequestMethod.OPTIONS,
    RequestMethod.GET,
    RequestMethod.PUT,
    RequestMethod.DELETE,
    RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@Validated
@RequestMapping("/api/bookmark")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("")
    public DataResponse getBookmarkByUserId(@RequestParam(required = true) Integer userId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) throws HandleExceptionNotFound {
        return bookmarkService.getBookmarkByUserId(userId, page, size);
    }

    @PostMapping("")
    public DataResponse insertBookmark(@RequestParam(name = "bookId") Integer bb_bookId) {
        return bookmarkService.insertBookmark(bb_bookId);
    }

    @DeleteMapping("")
    public DataResponse deleteBookmark(@RequestParam(name = "bookId") Integer bb_bookId) {
        return bookmarkService.deleteBookmark(bb_bookId);
    }

    @DeleteMapping("/{bookmarkId}")
    public DataResponse deleteBookmarkById(@PathVariable Integer bookmarkId) {
        return bookmarkService.deleteBookmarkById(bookmarkId);
    }
    
}
