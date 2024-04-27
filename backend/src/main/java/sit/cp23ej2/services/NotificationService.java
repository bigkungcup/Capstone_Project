package sit.cp23ej2.services;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Notification.CreateNotificationDTO;
import sit.cp23ej2.dtos.Notification.NotificationDTO;
import sit.cp23ej2.dtos.User.UserReportDTO;
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
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    public DataResponse getNotificationByUserId(Integer notificationLevel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        List<Notification> notificationByUserId = repository.getNotificationByUserIdAndLevel(user.getUserId(), notificationLevel);

        List<NotificationDTO> notification = new ArrayList<>();

        notificationByUserId.forEach(noti -> {
            NotificationDTO notificationDTO = modelMapper.map(noti, NotificationDTO.class);

            Duration duration = Duration.between(LocalDateTime.now(), noti.getNotificationCreateDateTime());
            notificationDTO.setCountDateTime(Math.abs(duration.toSeconds()));

            if (noti.getNotificationType().equals("Bookmark")) {
                String[] link = noti.getNotificationLink().split("/");
                Book bookById = bookRepository.getBookById(Integer.parseInt(link[2]));
                if(bookById != null){
                    notificationDTO.setBook(modelMapper.map(bookById, BookDTO.class));
                    notificationDTO.getBook().setBookTag(notificationDTO.getBook().getBookTag().replaceAll(",", ", "));
                    notificationDTO.getBook().setBookTagList(new ArrayList<String>(Arrays.asList(notificationDTO.getBook().getBookTag().split(", "))));
                    try {
                        Path pathFile = fileStorageService.load(notificationDTO.getBook());
                        if (pathFile != null) {
                            notificationDTO.getBook().setFile(baseUrl + "/api/files/filesBook/" + notificationDTO.getBook().getBookId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notification.add(notificationDTO);
                }else{
                    repository.deleteNotificationById(noti.getNotificationId());
                }
            }

            if (noti.getNotificationType().equals("Review")) {
                String[] link = noti.getNotificationLink().split("/");
                User userById = userRepository.getUserById(Integer.parseInt(link[2]));
                if(userById != null){
                    notificationDTO.setUser(modelMapper.map(userById, UserReportDTO.class));
                    try {
                        Path pathFile = fileStorageService.loadUserFile(userById.getUserId());
                        if (pathFile != null) {
                            notificationDTO.getUser().setFile(baseUrl + "/api/files/filesUser/" + userById.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notificationDTO.setNotificationLink("/book/" + link[3] + "/");
                    notification.add(notificationDTO);
                }else{
                    repository.deleteNotificationById(noti.getNotificationId());
                }
            }

            if(noti.getNotificationType().equals("Follow")){
                String[] link = noti.getNotificationLink().split("/");
                User userById = userRepository.getUserById(Integer.parseInt(link[2]));
                if(userById != null){
                    notificationDTO.setUser(modelMapper.map(userById, UserReportDTO.class));
                    try {
                        Path pathFile = fileStorageService.loadUserFile(userById.getUserId());
                        if (pathFile != null) {
                            notificationDTO.getUser().setFile(baseUrl + "/api/files/filesUser/" + userById.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notification.add(notificationDTO);
                }else{
                    repository.deleteNotificationById(noti.getNotificationId());
                }
            }
        });

        if(notification.size() == 0){
            throw new HandleExceptionNotFound("Notification Not Found", "Notification");
        }

        return responseWithData(notification, 200, "OK", "All Notification");
    }

    public DataResponse getCountNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        Integer countNotification = repository.getCountNotification(user.getUserId());
        HashMap<String, Integer> countNotificationMap = new HashMap<>();
        countNotificationMap.put("countNotification", countNotification);
        return responseWithData(countNotificationMap, 200, "OK", "Count Notification");
    }

    public DataResponse getCountNotificationNormal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        Integer countNotification = repository.getCountNotificationNormal(user.getUserId());
        HashMap<String, Integer> countNotificationMap = new HashMap<>();
        countNotificationMap.put("countNotificationNormal", countNotification);
        return responseWithData(countNotificationMap, 200, "OK", "Count Notification Normal");
    }

    public DataResponse getCountNotificationSystem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        Integer countNotification = repository.getCountNotificationSystem(user.getUserId());
        HashMap<String, Integer> countNotificationMap = new HashMap<>();
        countNotificationMap.put("countNotificationSystem", countNotification);
        return responseWithData(countNotificationMap, 200, "OK", "Count Notification System");
    }

    public DataResponse createNotification(CreateNotificationDTO createNotificationDTO) {

        userRepository.getAllUserList().forEach(u -> {
            repository.insertNotification(u.getUserId(), createNotificationDTO.getNotificationTitle(),
                    createNotificationDTO.getNotificationDetail(), 0, 1, createNotificationDTO.getNotificationLink(),
                    createNotificationDTO.getNotificationType());

            try {
                System.out.println("Send Email");
                Map<String, Object> variables = new HashMap<>();
                variables.put("detail", createNotificationDTO.getNotificationDetail());
                emailService.sendEmail(u.getEmail(), createNotificationDTO.getNotificationTitle(),
                        "System Maintenance", variables);
            } catch (AddressException e) {
                System.out.println("Address Exception" + e.getMessage());
                e.printStackTrace();
            } catch (MessagingException e) {
                System.out.println("Messaging Exception" + e.getMessage());
                e.printStackTrace();
            }

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
