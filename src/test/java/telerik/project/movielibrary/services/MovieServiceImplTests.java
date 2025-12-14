package telerik.project.movielibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.repositories.MovieRepository;
import telerik.project.movielibrary.services.contracts.OmdbService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTests {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private OmdbService omdbService;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie existingMovie;

    @BeforeEach
    void setUp() {
        existingMovie = new Movie();
        existingMovie.setId(1L);
        existingMovie.setTitle("Old Title");
        existingMovie.setDirector("Someone");
        existingMovie.setReleaseYear(2000);
    }

    @Test
    void getAll_shouldReturnAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(existingMovie));

        List<Movie> result = movieService.getAll();

        assertEquals(1, result.size());
        assertSame(existingMovie, result.get(0));
        verify(movieRepository).findAll();
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void getById_whenExists_shouldReturnMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));

        Movie result = movieService.getById(1L);

        assertSame(existingMovie, result);
        verify(movieRepository).findById(1L);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void getById_whenMissing_shouldThrowEntityNotFoundException() {
        when(movieRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> movieService.getById(42L));

        verify(movieRepository).findById(42L);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void getByTitle_whenExists_shouldReturnMovie() {
        when(movieRepository.findByTitle("Old Title"))
                .thenReturn(Optional.of(existingMovie));

        Movie result = movieService.getByTitle("Old Title");

        assertSame(existingMovie, result);
        verify(movieRepository).findByTitle("Old Title");
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void getByTitle_whenMissing_shouldThrowEntityNotFoundException() {
        when(movieRepository.findByTitle("Missing"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> movieService.getByTitle("Missing"));

        verify(movieRepository).findByTitle("Missing");
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void create_whenTitleFree_shouldSaveAndEnrich() {
        Movie newMovie = new Movie();
        newMovie.setTitle("New Movie");

        when(movieRepository.existsByTitle("New Movie")).thenReturn(false);

        movieService.create(newMovie);

        verify(movieRepository).existsByTitle("New Movie");
        verify(movieRepository).save(newMovie);
        verify(omdbService).enrichMovieWithRating(newMovie);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void create_whenTitleTaken_shouldThrowEntityDuplicateException() {
        Movie newMovie = new Movie();
        newMovie.setTitle("Taken");

        when(movieRepository.existsByTitle("Taken")).thenReturn(true);

        assertThrows(EntityDuplicateException.class,
                () -> movieService.create(newMovie));

        verify(movieRepository).existsByTitle("Taken");
        verify(movieRepository, never()).save(any());
        verify(omdbService, never()).enrichMovieWithRating(any());
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void update_whenMovieMissing_shouldThrowEntityNotFoundException() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        Movie updated = new Movie();
        updated.setTitle("Whatever");

        assertThrows(EntityNotFoundException.class,
                () -> movieService.update(99L, updated));

        verify(movieRepository).findById(99L);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void update_whenTitleChangedAndFree_shouldCheckAndSave() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle("New Title")).thenReturn(false);

        Movie updated = new Movie();
        updated.setTitle("New Title");
        updated.setDirector("New Director");

        movieService.update(1L, updated);

        assertEquals("New Title", existingMovie.getTitle());
        assertEquals("New Director", existingMovie.getDirector());

        verify(movieRepository).findById(1L);
        verify(movieRepository).existsByTitle("New Title");
        verify(movieRepository).save(existingMovie);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void update_whenTitleChangedButTaken_shouldThrowEntityDuplicateException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle("Taken")).thenReturn(true);

        Movie updated = new Movie();
        updated.setTitle("Taken");

        assertThrows(EntityDuplicateException.class,
                () -> movieService.update(1L, updated));

        verify(movieRepository).findById(1L);
        verify(movieRepository).existsByTitle("Taken");
        verify(movieRepository, never()).save(any());
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));

        movieService.delete(1L);

        verify(movieRepository).findById(1L);
        verify(movieRepository).delete(existingMovie);
        verifyNoMoreInteractions(movieRepository, omdbService);
    }

    @Test
    void delete_whenMissing_shouldThrowEntityNotFoundException() {
        when(movieRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> movieService.delete(123L));

        verify(movieRepository).findById(123L);
        verify(movieRepository, never()).delete(any());
        verifyNoMoreInteractions(movieRepository, omdbService);
    }
}