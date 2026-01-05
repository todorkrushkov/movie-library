package telerik.project.movielibrary.models.dtos.watchedmovie;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WatchedMovieNoteDTO {
    @Size(max = 1024, message = "Note must be max 1024 characters long.")
    private String note;
}
