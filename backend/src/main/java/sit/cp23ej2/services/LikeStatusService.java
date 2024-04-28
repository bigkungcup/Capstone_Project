package sit.cp23ej2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.LikeStatus.CreateReviewLikeStatus;
import sit.cp23ej2.dtos.LikeStatus.UpdateReviewLikeStatus;
import sit.cp23ej2.entities.LikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.LikeStatusRepository;
import sit.cp23ej2.repositories.ReviewRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class LikeStatusService extends CommonController {

    @Autowired
    private LikeStatusRepository repository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

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
            if(param.getLikeStatus() == 1){
                reviewRepository.increaseReviewTotalLike(param.getReviewId());
                review.setReviewTotalLike(review.getReviewTotalLike() + 1);
                userRepository.increaseTotalLike(param.getUserId());
                return responseWithData(review, 201, "Created", "Review TotalLike Updated");
            }else if(param.getLikeStatus() == 2){
                reviewRepository.increaseReviewTotalDisLike(param.getReviewId());
                review.setReviewTotalDisLike(review.getReviewTotalDisLike() + 1);
                return responseWithData(review, 201, "Created", "Review TotalDisLike Updated");
            }else{
                throw new HandleExceptionBadRequest("Invalid LikeStatus");
            }
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

        User user = userRepository.getUserById(param.getUserId());

        if (user == null) {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        LikeStatus likeStatus = repository.getLikeStatusById(likeStatusId);;

        if (param.getLikeStatus() == 1 && likeStatus.getLikeStatus() == 2) {
            repository.updateLikeStatus(param.getLikeStatus(), likeStatusId);
            reviewRepository.increaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() + 1);
            reviewRepository.decreaseReviewTotalDisLike(param.getReviewId());
            review.setReviewTotalDisLike(review.getReviewTotalDisLike() - 1);
            userRepository.increaseTotalLike(param.getUserId());
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else if (param.getLikeStatus() == 2 && likeStatus.getLikeStatus() == 1) {
            repository.updateLikeStatus(param.getLikeStatus(), likeStatusId);
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            reviewRepository.increaseReviewTotalDisLike(param.getReviewId());
            review.setReviewTotalDisLike(review.getReviewTotalDisLike() + 1);
            userRepository.decreaseTotalLike(param.getUserId());
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else if( param.getLikeStatus() == 3 && likeStatus.getLikeStatus() == 1){
            repository.deleteLikeStatus(likeStatusId);
            reviewRepository.decreaseReviewTotalLike(param.getReviewId());
            review.setReviewTotalLike(review.getReviewTotalLike() - 1);
            userRepository.decreaseTotalLike(param.getUserId());
            return responseWithData(review, 200, "OK", "Review TotalLike Updated");
        } else if( param.getLikeStatus() == 3 && likeStatus.getLikeStatus() == 2){
            repository.deleteLikeStatus(likeStatusId);
            reviewRepository.decreaseReviewTotalDisLike(param.getReviewId());
            review.setReviewTotalDisLike(review.getReviewTotalDisLike() - 1);
            return responseWithData(review, 200, "OK", "Review TotalDisLike Updated");
        }else{
            throw new HandleExceptionBadRequest("Invalid LikeStatus");
        }
    }
}
