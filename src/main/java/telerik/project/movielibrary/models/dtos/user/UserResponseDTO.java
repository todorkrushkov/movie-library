package telerik.project.movielibrary.models.dtos.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String username;

    private String role;

}
