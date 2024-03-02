package sit.cp23ej2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.LikeStatus.CreateReviewLikeStatus;
import sit.cp23ej2.dtos.LikeStatus.UpdateReviewLikeStatus;
import sit.cp23ej2.entities.LikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.LikeStatusRepository;
import sit.cp23ej2.repositories.ReviewRepository;

@Service
public class LikeStatusService extends CommonController {

    @Autowired
    private LikeStatusRepository repository;

    @Autowired
    private ReviewRepository reviewRepository;

    public DataResponse createLikeStatus(CreateReviewLikeStatus param) {

        repository.getLikeStatus(param.getUserId()).forEach(likeStatus -> {
            if (likeStatus.getLsr_reviewId() == param.getReviewId() ) {
                throw new HandleExceptionBadRequest("You already like this review");
            }
        });

        Review review = reviewRepository.getReviewById(param.getReviewId());

        if (review == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        } else {
            repository.insertLikeStatus(param.getReviewId(), param.getUserId(), param.getLikeStatus());
            reviewRepository.increaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() + 1);
            return responseWithData(review, 201, "Created", "Review TotalLike Updated");
        }
    }

    public DataResponse updateLikeStatus(Integer likeStatusId, UpdateReviewLikeStatus param) {

        if(!repository.existsByLikeStatusId(likeStatusId)){
            throw new HandleExceptionNotFound("LikeStatus Not Found", "LikeStatus");
        }

        Review review = reviewRepository.getReviewById(param.getReviewId());

        if (review == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        LikeStatus likeStatus = repository.getLikeStatusById(likeStatusId);;

        if (param.getLikeStatus() == 1 && likeStatus.getLikeStatus() == 2) {
            repository.updateLikeStatus(param.getLikeStatus(), likeStatusId);
            reviewRepository.increaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() + 1);
            reviewRepository.decreaseReviewTotalDisLike(param.getReviewId());
            review.setReviewTotalDisLike(review.getReviewTotalDisLike() - 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else if (param.getLikeStatus() == 2 && likeStatus.getLikeStatus() == 1) {
            repository.updateLikeStatus(param.getLikeStatus(), likeStatusId);
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            reviewRepository.increaseReviewTotalDisLike(param.getReviewId());
            review.setReviewTotalDisLike(review.getReviewTotalDisLike() + 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else {
            repository.deleteLikeStatus(likeStatusId);
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        }
    }
}
