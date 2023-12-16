package sit.cp23ej2.services;

import java.time.Instant;
// import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
// import sit.cp23ej2.dtos.Paginate;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.ReviewRepository;

@Service
public class ReviewService extends CommonController {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public DataResponse getReviewByBookId(int bookId, int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        PageReviewDTO reviews = modelMapper.map(repository.getReviewByBookId(bookId, pageable), PageReviewDTO.class);

        if (reviews.getContent().size() > 0) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Reviews");
            response.setResponse_datetime(Instant.now());
            response.setData(reviews);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        return response;
    }

    public DataResponse getReviewById(int reviewId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Review review = repository.getReviewById(reviewId);
        if (review != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review");
            response.setResponse_datetime(Instant.now());
            response.setData(review);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
        return response;
    }

    public DataResponse createReviewByBookId(CreateReviewDTO review) {
        DataResponse response = new DataResponse();
        repository.insertReview(review.getRating(), review.getDetail(), review.getTitle(), review.getUserId(),
                review.getBookId(), review.getSpoileFlag());
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Review Created");
        response.setResponse_datetime(Instant.now());
        return response;
    }

    public DataResponse updateReviewByBookId(UpdateReviewDTO review, Integer reviewId) {
        DataResponse response = new DataResponse();
        repository.updateReview(review.getRating(), review.getDetail(), review.getTitle(), review.getSpoileFlag(),
                reviewId);
        Review dataReview = repository.getReviewById(reviewId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Review Updated");
        response.setResponse_datetime(Instant.now());
        response.setData(dataReview);
        return response;
    }

    // public DataResponse updateReviewTotalLikeAndTotalDisLike(UpdateReviewDTO review, Integer reviewId) {
    //     DataResponse response = new DataResponse();
    //     repository.updateTotalLikeAndTotalDisLike(review.getTotalLike(), review.getTotalDisLike(), reviewId);
    //     Review dataReview = repository.getReviewById(reviewId);
    //     response.setResponse_code(200);
    //     response.setResponse_status("OK");
    //     response.setResponse_message("Review TotalLike and TotalDislike Updated");
    //     response.setResponse_datetime(Instant.now());
    //     response.setData(dataReview);
    //     return response;
    // }

    public DataResponse deleteReviewByBookId(int reviewId) {
        DataResponse response = new DataResponse();
        repository.deleteReview(reviewId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Review Deleted");
        response.setResponse_datetime(Instant.now());
        return response;
    }

}
