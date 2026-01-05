package telerik.project.movielibrary.models.dtos.watchedmovie;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class WatchedMovieResponseDTO {

    private Long movieId;
    private String title;
    private Double rating;
    private String note;
    private LocalDate watchedAt;
}
