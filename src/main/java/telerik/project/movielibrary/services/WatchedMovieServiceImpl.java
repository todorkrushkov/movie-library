package telerik.project.movielibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.WatchedMovie;
import telerik.project.movielibrary.repositories.WatchedMovieRepository;
import telerik.project.movielibrary.services.contracts.MovieService;
import telerik.project.movielibrary.services.contracts.UserService;
import telerik.project.movielibrary.services.contracts.WatchedMovieService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchedMovieServiceImpl implements WatchedMovieService {

    private final WatchedMovieRepository watchedMovieRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Override
    public List<WatchedMovie> getWatchedMovies(Long userId) {
        userService.getById(userId);
        return watchedMovieRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public void addWatchedMovie(Long userId, Long movieId, String note) {
        Movie movie = movieService.getById(movieId);
        User user = userService.getById(userId);
        if(watchedMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new EntityDuplicateException("Watched movie", "title", movie.getTitle());
        }

        WatchedMovie watchedMovie = new WatchedMovie();
        watchedMovie.setUser(user);
        watchedMovie.setMovie(movie);
        watchedMovie.setNote(note);
        watchedMovie.setWatchedAt(LocalDate.now());

        watchedMovieRepository.save(watchedMovie);
    }

    @Override
    @Transactional
    public void updateNote(Long userId, Long movieId, String note) {
        Movie movie = movieService.getById(movieId);
        userService.getById(userId);
        WatchedMovie watchedMovie = watchedMovieRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new EntityNotFoundException("Watched movie", "title", movie.getTitle()));

        watchedMovie.setNote(note);
    }
}
