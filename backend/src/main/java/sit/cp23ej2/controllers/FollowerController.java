package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.services.FollowerService;

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
@RequestMapping("/api/follower")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @GetMapping("/follower")
    public DataResponse getFollowers(@RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return followerService.getFollowers(page, size);
    }

    @GetMapping("/following")
    public DataResponse getFollowersByGuest(@RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return followerService.getFollowing(page, size);
    }

    @PostMapping("")
    public DataResponse insertFollower(@RequestParam(name = "userFollowerId") Integer userFollowerId) {
        return followerService.insertFollower(userFollowerId);
    }

    @DeleteMapping("{userFollowerId}")
    public DataResponse deleteFollower(@PathVariable Integer userFollowerId) {
        return followerService.deleteFollower(userFollowerId);
    }
    
}
