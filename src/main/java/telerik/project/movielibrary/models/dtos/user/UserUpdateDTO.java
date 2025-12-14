package telerik.project.movielibrary.models.dtos.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDTO {

    @Size(min = 3, max = 32)
    private String username;

    @Size(max = 255)
    private String password;

}
