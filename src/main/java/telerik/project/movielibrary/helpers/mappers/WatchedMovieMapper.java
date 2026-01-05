package telerik.project.movielibrary.helpers.mappers;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.models.WatchedMovie;
import telerik.project.movielibrary.models.dtos.watchedmovie.WatchedMovieResponseDTO;

@Component
public class WatchedMovieMapper {

    public WatchedMovieResponseDTO toResponse(WatchedMovie watchedMovie) {
        return new WatchedMovieResponseDTO(
                watchedMovie.getMovie().getId(),
                watchedMovie.getMovie().getTitle(),
                watchedMovie.getMovie().getRating(),
                watchedMovie.getNote(),
                watchedMovie.getWatchedAt()
        );
    }
}
