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
import sit.cp23ej2.services.FollowService;

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
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followerService;

    @GetMapping("/follower")
    public DataResponse getFollowers(@RequestParam(required = true) Integer userId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return followerService.getFollowers(userId, page, size);
    }

    @GetMapping("/following")
    public DataResponse getFollowersByGuest(@RequestParam(required = true) Integer userId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return followerService.getFollowing(userId, page, size);
    }

    @PostMapping("")
    public DataResponse insertFollower(@RequestParam(name = "userFollowingId") Integer userFollowingId) {
        return followerService.insertFollowing(userFollowingId);
    }

    @DeleteMapping("{userFollowingId}")
    public DataResponse deleteFollower(@PathVariable Integer userFollowingId) {
        return followerService.deleteFollowing(userFollowingId);
    }
    
}
