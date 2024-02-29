package sit.cp23ej2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Review.CreateReviewLikeStatus;
import sit.cp23ej2.dtos.Review.UpdateReviewLikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.CheckLikeReviewRepository;
import sit.cp23ej2.repositories.ReviewRepository;

@Service
public class CheckLikeReviewService extends CommonController {

    @Autowired
    private CheckLikeReviewRepository repository;

    @Autowired
    private ReviewRepository reviewRepository;

    public DataResponse createLikeStatus(CreateReviewLikeStatus param) {
        Review review = reviewRepository.getReviewById(param.getReviewId());

        if (review == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        } else {
            repository.insertCheckLikeReview(param.getReviewId(), param.getUserId(), param.getLikeStatus());
            reviewRepository.increaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() + 1);
            return responseWithData(review, 201, "Created", "Review TotalLike Updated");
        }
    }

    public DataResponse updateLikeStatus(UpdateReviewLikeStatus param) {
        Review review = reviewRepository.getReviewById(param.getReviewId());

        if (review == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        if (param.getLikeStatus() == 1) {
            repository.updateCheckLikeReview(param.getLikeStatus(), param.getCheckLikeReviewId());
            reviewRepository.increaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() + 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else if (param.getLikeStatus() == 2) {
            repository.updateCheckLikeReview(param.getLikeStatus(), param.getCheckLikeReviewId());
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else {
            repository.deleteCheckLikeReview(param.getCheckLikeReviewId());
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        }
    }
}
