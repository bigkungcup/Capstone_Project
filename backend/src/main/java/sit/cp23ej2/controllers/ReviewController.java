package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
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

   @GetMapping("")
   public DataResponse getAllBook(@NotNull @RequestParam(required = false) Integer bookId,
         @NotNull @RequestParam(defaultValue = "0", required = false) Integer page,
         @RequestParam(defaultValue = "10", required = false) Integer size,
         @RequestParam(required = false) Long reviewRating,
         @RequestParam(required =  false) String  sortBy, 
         @RequestParam(required =  false) String sortType) throws HandleExceptionNotFound {
      if (bookId == 0 || bookId == null) {
         throw new HandleExceptionBadRequest("Book Id is required");
      }
      return reviewService.getReviewByBookId(bookId, page, size, reviewRating, sortBy, sortType);
   }

   @GetMapping("/guest")
   public DataResponse getAllBookByGuest(@NotNull @RequestParam(required = false) Integer bookId,
         @NotNull @RequestParam(defaultValue = "0", required = false) Integer page,
         @RequestParam(defaultValue = "10", required = false) Integer size,
         @RequestParam(required = false) Long reviewRating,
         @RequestParam(required =  false) String  sortBy, 
         @RequestParam(required =  false) String sortType) throws HandleExceptionNotFound {
      if (bookId == 0 || bookId == null) {
         throw new HandleExceptionBadRequest("Book Id is required");
      }
      return reviewService.getReviewByBookId(bookId, page, size, reviewRating, sortBy, sortType);
   }

   @GetMapping("/{reviewId}")
   public DataResponse getReviewById(@PathVariable Integer reviewId) throws HandleExceptionNotFound {
      return reviewService.getReviewById(reviewId);
   }

   @GetMapping("/guest/{reviewId}")
   public DataResponse getReviewByIdGuest(@PathVariable Integer reviewId) throws HandleExceptionNotFound {
      return reviewService.getReviewById(reviewId);
   }

   @GetMapping("/me")
   public DataResponse getReviewByUserId(@RequestParam(required = true) Integer userId,
         @RequestParam(defaultValue = "0", required = false) Integer userIdView,
         @RequestParam(defaultValue = "0", required = false) Integer page,
         @RequestParam(defaultValue = "10", required = false) Integer size) throws HandleExceptionNotFound {
      return reviewService.getReviewByUserId(userId, userIdView, page, size);
   }

   @GetMapping("/newReview")
   public DataResponse getReviewByCreateDateTime() {
      return reviewService.getReviewByCreateDateTime();
   }

   @GetMapping("/ratingCount")
   public DataResponse getReviewRatingCount() {
      return reviewService.getReviewRatingCount();
   }

   @PostMapping("")
   @ResponseStatus(HttpStatus.CREATED)
   public DataResponse createReview(@RequestBody @Valid CreateReviewDTO review) {
      return reviewService.createReviewByBookId(review);
   }

   @PutMapping("/{reviewId}")
   public DataResponse updateReview(@RequestBody @Valid UpdateReviewDTO review, @PathVariable Integer reviewId) {
      return reviewService.updateReviewByBookId(review, reviewId);
   }

   @DeleteMapping("/{reviewId}")
   public DataResponse deleteReview(@PathVariable Integer reviewId) {
      return reviewService.deleteReviewByBookId(reviewId);
   }
}
