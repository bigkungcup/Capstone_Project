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
import sit.cp23ej2.dtos.LikeStatus.CreateReviewLikeStatus;
import sit.cp23ej2.dtos.LikeStatus.UpdateReviewLikeStatus;
import sit.cp23ej2.services.LikeStatusService;

@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://capstone23.sit.kmutt.ac.th/ej2"
}, methods = {
    RequestMethod.OPTIONS,
    RequestMethod.GET,
    RequestMethod.PUT,
    RequestMethod.DELETE,
    RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@Validated
@RequestMapping("/api/likeStatus")
public class LikeStatusController {

    @Autowired
    private LikeStatusService checkLikeReviewService;

    @PostMapping("")
    public DataResponse createLikeStatus(@RequestBody CreateReviewLikeStatus param) {
        return checkLikeReviewService.createLikeStatus(param);
    }

    @PutMapping("/{likeStatusId}")
    public DataResponse deleteLikeStatus(@PathVariable Integer likeStatusId, @RequestBody UpdateReviewLikeStatus param) {
        return checkLikeReviewService.updateLikeStatus(likeStatusId, param);
    }
    
}
