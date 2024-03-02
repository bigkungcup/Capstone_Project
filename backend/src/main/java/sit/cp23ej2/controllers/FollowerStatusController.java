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
import sit.cp23ej2.dtos.FollowerStatus.CreateFollowerStatus;
import sit.cp23ej2.dtos.FollowerStatus.UpdateFollowerStatus;
import sit.cp23ej2.services.FollowerStatusService;

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
@RequestMapping("/api/followerStatus")
public class FollowerStatusController {

    @Autowired
    private FollowerStatusService followerStatusService;

    @PostMapping("")
    public DataResponse createFollowerStatus(@RequestBody CreateFollowerStatus param) {
        return followerStatusService.insertFollowerStatus(param);
    }

    @PutMapping("/{followerStatusId}")
    public DataResponse updateFollowerStatus(@PathVariable Integer followerStatusId,@RequestBody UpdateFollowerStatus param) {
        return followerStatusService.updateFollowerStatus(followerStatusId, param);
    }
    
}
