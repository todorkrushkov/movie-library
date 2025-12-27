package telerik.project.movielibrary.models.dtos.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;
}
