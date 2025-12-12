package telerik.project.movielibrary.models.dtos.movie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieCreateDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String director;

    @Min(1888)
    @Max(2100)
    private Integer releaseYear;

}
