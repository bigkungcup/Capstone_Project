package sit.cp23ej2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Notification.CreateNotificationDTO;
import sit.cp23ej2.entities.Notification;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.NotificationRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class NotificationService extends CommonController {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private UserRepository userRepository;

    public DataResponse getNotificationByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        List<Notification> notificationByUserId = repository.getNotificationByUserId(user.getUserId());

        return responseWithData(notificationByUserId, 200,"OK", "All Notification");
    }

    public DataResponse createNotification(CreateNotificationDTO createNotificationDTO) {

        userRepository.getAllUserList().forEach(u -> {
            repository.insertNotification(u.getUserId(), createNotificationDTO.getNotificationTitle(), createNotificationDTO.getNotificationDetail(), 0, 1, createNotificationDTO.getNotificationLink(), createNotificationDTO.getNotificationType());
        });
        
        return response(201, "Created", "Notification Created");
        // repository.insertNotification(user.getUserId(), createNotificationDTO.getNotificationTitle(), createNotificationDTO.getNotificationDetail(), 0, 1, createNotificationDTO.getNotificationLink(), createNotificationDTO.getNotificationType());
        // repository.insertNotification(user.getUserId(), notification.getNotificationTitle(), notification.getNotificationDetail(), notification.getNotificationStatus(), notification.getNotificationLevel(), notification.getNotificationLink(), notification.getNotificationType());
    }

    public DataResponse updateAllNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        repository.getNotificationByUserId(user.getUserId()).forEach(n -> {
            repository.updateNotification(1, n.getNotificationId());
        });
        return response(200, "OK", "Notification Updated");
    }

    public DataResponse updateNotification(Integer notificationId) {
        if(!repository.existsByNotificationId(notificationId)){
            throw new HandleExceptionNotFound("Notification Not Found", "Notification");
        }
        repository.updateNotification(1, notificationId);
        return response(200, "OK", "Notification Updated");
    }

}
