package sit.cp23ej2.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResetPasswordDTO {

    @NotBlank(message = "Password shouldn't be blank or null")
    @Size(max = 16, message = "Password shouldn't be more than 16 characters")
    private String password;

    @NotBlank(message = "New Password shouldn't be blank or null")
    @Size(max = 16, message = "New Password shouldn't be more than 16 characters")
    private String newPassword;
}
