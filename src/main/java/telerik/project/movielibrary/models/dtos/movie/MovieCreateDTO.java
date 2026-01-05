package telerik.project.movielibrary.models.dtos.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieCreateDTO {

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be max 255 characters long.")
    private String title;

}
