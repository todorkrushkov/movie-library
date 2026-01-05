package telerik.project.movielibrary.models.dtos.movie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MovieResponseDTO {

    private Long id;

    private String title;

    private String director;

    private LocalDate releaseDate;

    private Double rating;

}
