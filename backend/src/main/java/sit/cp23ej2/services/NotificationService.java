package sit.cp23ej2.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Notification.CreateNotificationDTO;
import sit.cp23ej2.dtos.Notification.NotificationDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.Notification;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.NotificationRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class NotificationService extends CommonController {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    public DataResponse getNotificationByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        List<Notification> notificationByUserId = repository.getNotificationByUserId(user.getUserId());

        List<NotificationDTO> notificationDTO = new ArrayList<>();

        notificationByUserId.forEach(noti -> {
            NotificationDTO dto = modelMapper.map(noti, NotificationDTO.class);
            if (noti.getNotificationType().equals("Bookmark") || noti.getNotificationType().equals("Review")) {
                String[] link = noti.getNotificationLink().split("/");
                Book bookById = bookRepository.getBookById(Integer.parseInt(link[2]));
                dto.setBook(modelMapper.map(bookById, BookDTO.class));
                dto.getBook().setBookTag(dto.getBook().getBookTag().replaceAll(",", ", "));
                dto.getBook().setBookTagList(new ArrayList<String>(Arrays.asList(dto.getBook().getBookTag().split(", "))));
                try {
                    Path pathFile = fileStorageService.load(dto.getBook());
                    if (pathFile != null) {
                        dto.getBook().setFile(baseUrl + "/api/files/filesBook/" + dto.getBook().getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(noti.getNotificationType().equals("Follow")){
                String[] link = noti.getNotificationLink().split("/");
                User userById = userRepository.getUserById(Integer.parseInt(link[2]));
                dto.setUser(modelMapper.map(userById, UserDTO.class));
                try {
                    if (userById != null) {
                        dto.getUser().setFile(baseUrl + "/api/files/filesUser/" + userById.getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            notificationDTO.add(dto);
        });

        return responseWithData(notificationDTO, 200, "OK", "All Notification");
    }

    public DataResponse createNotification(CreateNotificationDTO createNotificationDTO) {

        userRepository.getAllUserList().forEach(u -> {
            repository.insertNotification(u.getUserId(), createNotificationDTO.getNotificationTitle(),
                    createNotificationDTO.getNotificationDetail(), 0, 1, createNotificationDTO.getNotificationLink(),
                    createNotificationDTO.getNotificationType());
        });

        return response(201, "Created", "Notification Created");
        // repository.insertNotification(user.getUserId(),
        // createNotificationDTO.getNotificationTitle(),
        // createNotificationDTO.getNotificationDetail(), 0, 1,
        // createNotificationDTO.getNotificationLink(),
        // createNotificationDTO.getNotificationType());
        // repository.insertNotification(user.getUserId(),
        // notification.getNotificationTitle(), notification.getNotificationDetail(),
        // notification.getNotificationStatus(), notification.getNotificationLevel(),
        // notification.getNotificationLink(), notification.getNotificationType());
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
        if (!repository.existsByNotificationId(notificationId)) {
            throw new HandleExceptionNotFound("Notification Not Found", "Notification");
        }
        repository.updateNotification(1, notificationId);
        return response(200, "OK", "Notification Updated");
    }

}
