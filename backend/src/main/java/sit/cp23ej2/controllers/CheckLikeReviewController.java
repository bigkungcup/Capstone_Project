package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Review.CreateReviewLikeStatus;
import sit.cp23ej2.dtos.Review.UpdateReviewLikeStatus;
import sit.cp23ej2.services.CheckLikeReviewService;

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
@RequestMapping("/api/checkLikeReview")
public class CheckLikeReviewController {

    @Autowired
    private CheckLikeReviewService checkLikeReviewService;


    @PostMapping("/like")
    public DataResponse createLikeStatus(CreateReviewLikeStatus param) {
        return checkLikeReviewService.createLikeStatus(param);
    }

    @PutMapping("/updateLike")
    public DataResponse deleteLikeStatus(UpdateReviewLikeStatus param) {
        return checkLikeReviewService.updateLikeStatus(param);
    }
    
}
