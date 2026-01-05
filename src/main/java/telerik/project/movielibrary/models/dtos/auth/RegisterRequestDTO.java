package telerik.project.movielibrary.models.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 72, message = "Password must be between 6 and 72.")
    private String password;
}
