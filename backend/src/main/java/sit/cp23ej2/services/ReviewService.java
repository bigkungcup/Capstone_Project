package sit.cp23ej2.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Paginate;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.ReviewRepository;

@Service
public class ReviewService extends CommonController {

    @Autowired
    private ReviewRepository repository;

    public DataResponse getReviewByBookId(int page, int limit, int bookId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        List<Review> reviews = repository.getReviewByBookId(bookId);
        if (reviews != null) {
            Paginate pagination = this.paginate(page, limit, reviews);
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Reviews");
            response.setResponse_datetime(Instant.now());
            response.setData(reviews);
            response.setPaginate(pagination);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
        return response;
    }
    
    public DataResponse createReviewByBookId(CreateReviewDTO review){
        DataResponse response = new DataResponse();
        repository.insertReview(review.getRating(), review.getDetail(), review.getTitle(), review.getUserId(), review.getBookId());
    
            
            // Integer generatedReviewId = review.getReviewId();
            // // Now you have the generated ID
            // System.out.println("Generated Review ID: " + generatedReviewId);
            // Review  dataReview = repository.getReviewById(review.getReviewId());
            // response.setData(dataReview);
        
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Review Created");
        response.setResponse_datetime(Instant.now());
        return response;
    }

    public DataResponse updateReviewByBookId(UpdateReviewDTO review, Integer reviewId){
        DataResponse response = new DataResponse();
        repository.updateReview(review.getRating(), review.getDetail(), review.getTitle(), review.getSpoileFlag(), reviewId);
        Review  dataReview = repository.getReviewById(review.getReviewId());
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Review Updated");
        response.setResponse_datetime(Instant.now());
        response.setData(dataReview);
        return response;
    }

    public DataResponse deleteReviewByBookId(int reviewId){
        DataResponse response = new DataResponse();
        repository.deleteReview(reviewId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Review Deleted");
        response.setResponse_datetime(Instant.now());
        return response;
    }

}
