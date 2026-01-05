package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.WatchedMovie;

import java.util.List;

public interface WatchedMovieService {

    List<WatchedMovie> getWatchedMovies(Long userId);

    void addWatchedMovie(Long userId, Long movieId, String note);

    void updateNote(Long userId, Long movieId, String note);

}
