package sit.cp23ej2.dtos.Notification;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.User.UserDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NotificationDTO {

    private Integer notificationId; 

    private String notificationTitle;

    private String notificationDetail;

    private Integer notificationLevel;

    private String notificationLink;

    private String notificationType;

    private Integer notificationStatus;

    private LocalDateTime notificationCreateDateTime;

    private LocalDateTime notificationUpdateDateTime;

    private UserDTO user;

    private BookDTO book;

    private Long countDateTime;
}

