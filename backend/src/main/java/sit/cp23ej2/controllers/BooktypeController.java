package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Booktype.CreateBooktypeDTO;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.BooktypeService;

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
@RequestMapping("/api/booktype")
public class BooktypeController {

    @Autowired
    private BooktypeService booktypeService;

    @GetMapping("")
    public DataResponse getAllBooktype() throws HandleExceptionNotFound {
        return booktypeService.getAllBooktype();
    }

    @PostMapping("")
    public DataResponse insertBooktype(@RequestBody CreateBooktypeDTO bookType) {
        return booktypeService.insertBooktype(bookType);
    }

    @DeleteMapping("/{booktypeId}")
    public DataResponse deleteBooktype(@PathVariable Integer booktypeId) {
        return booktypeService.deleteBooktype(booktypeId);
    }
    
}
