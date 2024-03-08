package sit.cp23ej2.dtos.Notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateNotificationDTO {

    @NotBlank(message = "Notification Title shouldn't be blank or null")
    private String notificationTitle;

    @NotBlank(message = "Notification Detail shouldn't be blank or null")
    private String notificationDetail;

    @NotBlank(message = "Notification Type shouldn't be blank or null")
    private String notificationType;

    private String notificationLink;

}
