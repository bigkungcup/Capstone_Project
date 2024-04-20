package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Notification.CreateNotificationDTO;
import sit.cp23ej2.services.NotificationService;

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
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping("")
    public DataResponse getNotificationByUserId(@RequestParam(defaultValue = "0") Integer notificationLevel) {
        return notificationService.getNotificationByUserId(notificationLevel);
    }

    @GetMapping("/count")
    public DataResponse getNotificationById() {
        return notificationService.getCountNotification();
    }

    @GetMapping("/countNormal")
    public DataResponse getNotificationNormalById() {
        return notificationService.getCountNotificationNormal();
    }

    @GetMapping("/countSystem")
    public DataResponse getNotificationSystemById() {
        return notificationService.getCountNotificationSystem();
    }

    @PostMapping("")
    public DataResponse createNotification(@RequestBody @Valid CreateNotificationDTO createNotificationDTO) {
        return notificationService.createNotification(createNotificationDTO);
    }

    @PutMapping("/{notificationId}")
    public DataResponse updateNotification(@PathVariable Integer notificationId) {
        return notificationService.updateNotification(notificationId);
    }

    @PutMapping("/updateAll")
    public DataResponse updateAllNotification() {
        return notificationService.updateAllNotification();
    }
    
}
