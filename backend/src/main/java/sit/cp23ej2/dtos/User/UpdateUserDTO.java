package sit.cp23ej2.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDTO {
    @NotBlank(message = "Display Name shouldn't be blank or null")
    private String displayName;

    @NotBlank(message = "Email shouldn't be blank or null")
    @Email(message = "Email should be valid")
    private String email;

    // @NotBlank(message = "Password shouldn't be blank or null")
    // @Size(max = 16, message = "Password shouldn't be more than 16 characters and less than 8 characters")
    // private String password;
    
    // @Size(max = 16, message = "Password shouldn't be more than 16 characters and less than 8 characters")
    // private String newPassword;

    private String bio;

    private String status;
}
