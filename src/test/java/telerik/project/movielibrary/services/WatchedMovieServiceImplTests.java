package telerik.project.movielibrary.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.WatchedMovie;
import telerik.project.movielibrary.repositories.WatchedMovieRepository;
import telerik.project.movielibrary.services.contracts.MovieService;
import telerik.project.movielibrary.services.contracts.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchedMovieServiceImplTests {

    @Mock
    private WatchedMovieRepository watchedMovieRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private WatchedMovieServiceImpl watchedMovieService;

    @Test
    void getWAtchedMovies_shouldReturnMovies() {
        when(userService.getById(1L)).thenReturn(new User());
        when(watchedMovieRepository.findAllByUserId(1L))
                .thenReturn(List.of(new WatchedMovie()));

        List<WatchedMovie> result = watchedMovieService.getWatchedMovies(1L);

        assertEquals(1, result.size());
        verify(userService).getById(1L);
        verify(watchedMovieRepository).findAllByUserId(1L);
    }

    @Test
    void addWatchedMovie_whenNotExists_shouldSave() {
        User user = new User();
        Movie movie = new Movie();

        when(userService.getById(1L)).thenReturn(user);
        when(movieService.getById(2L)).thenReturn(movie);
        when(watchedMovieRepository.existsByUserIdAndMovieId(1L, 2L))
                .thenReturn(false);

        watchedMovieService.addWatchedMovie(1L, 2L, "Nice");

        verify(watchedMovieRepository).save(any(WatchedMovie.class));
    }

    @Test
    void addWatchedMovie_whenExists_shouldThrowEntityDuplicateException() {
        when(userService.getById(1L)).thenReturn(new User());
        when(movieService.getById(2L)).thenReturn(new Movie());
        when(watchedMovieRepository.existsByUserIdAndMovieId(1L, 2L))
                .thenReturn(true);

        assertThrows(EntityDuplicateException.class,
                () -> watchedMovieService.addWatchedMovie(1L, 2L, "note"));
    }

    @Test
    void updateNote_whenExists_shouldUpdateNote() {
        WatchedMovie watchedMovie = new WatchedMovie();
        watchedMovie.setNote("old");

        when(movieService.getById(2L)).thenReturn(new Movie());
        when(userService.getById(1L)).thenReturn(new User());
        when(watchedMovieRepository.findByUserIdAndMovieId(1L, 2L))
                .thenReturn(Optional.of(watchedMovie));

        watchedMovieService.updateNote(1L, 2L,"new");

        assertEquals("new", watchedMovie.getNote());
        verify(watchedMovieRepository).findByUserIdAndMovieId(1L, 2L);
    }
}
