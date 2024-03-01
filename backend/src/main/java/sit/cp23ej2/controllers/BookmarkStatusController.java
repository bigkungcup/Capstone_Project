package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.BookmarkStatus.CreateBookmarkStatus;
import sit.cp23ej2.dtos.BookmarkStatus.UpdateBookmarkStatus;
import sit.cp23ej2.services.BookmarkStatusService;

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
@RequestMapping("/api/bookmarkStatus")
public class BookmarkStatusController {

    @Autowired
    private BookmarkStatusService bookmarkStatusService;

    @PostMapping("")
    public DataResponse createBookmarkStatus(@RequestBody CreateBookmarkStatus param) {
        return bookmarkStatusService.insertBookmarkStatus(param);
    }

    @PutMapping("/{bookmarkStatusId}")
    public DataResponse updateBookmarkStatus(@PathVariable Integer bookmarkStatusId, @RequestBody UpdateBookmarkStatus param) {
        return bookmarkStatusService.updateBookmarkStatus(bookmarkStatusId, param);
    }
    
}
