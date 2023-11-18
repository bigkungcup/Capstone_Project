package sit.cp23ej2.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.services.ReviewService;

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
@RequestMapping("/api/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @RequestMapping("")
    public DataResponse getAllBook(@NotNull @RequestParam(required = false) Integer page,
            @NotNull @RequestParam(required = false) Integer size,
            @NotNull @RequestParam(required = false) Integer bookId) throws HandleExceptionNotFound{
               DataResponse response = new DataResponse();
               
            if(bookId == 0 || bookId == null){
               response.setResponse_code(400);
               response.setResponse_status("Bad Request");
               response.setResponse_message("Book Id is required");
               response.setResponse_datetime(Instant.now());
               return response;
            }
       return reviewService.getReviewByBookId(page, size, bookId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse createReview(@RequestBody CreateReviewDTO review) {
       return reviewService.createReviewByBookId(review);
    }

    @PutMapping("/{reviewId}")
    public DataResponse updateReview(@RequestBody UpdateReviewDTO review, @PathVariable Integer reviewId) {
       return reviewService.updateReviewByBookId(review, reviewId);
    }

    @DeleteMapping("/{reviewId}")
      public DataResponse deleteReview(@PathVariable Integer reviewId) {
         return reviewService.deleteReviewByBookId(reviewId);
      }
}
