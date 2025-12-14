package telerik.project.movielibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import telerik.project.movielibrary.external.OmdbClient;
import telerik.project.movielibrary.external.OmdbMovieResponse;
import telerik.project.movielibrary.helpers.mappers.OmdbMapper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.repositories.MovieRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OmdbServiceImplTests {

    @Mock
    private OmdbClient omdbClient;

    @Mock
    private MovieRepository movieRepository;

    private OmdbMapper omdbMapper;

    private OmdbServiceImpl omdbService;

    @BeforeEach
    void setUp() {
        omdbMapper = new OmdbMapper();
        omdbService = new OmdbServiceImpl(omdbClient, omdbMapper, movieRepository);
    }

    @Test
    void enrichMovieWithRating_whenResponseTrue_shouldSetRatingAndSave() {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("True");
        response.setImdbRating(8.8);

        when(omdbClient.fetchByTitle("Inception")).thenReturn(response);

        omdbService.enrichMovieWithRating(movie);

        assertEquals(8.8, movie.getRating());

        verify(omdbClient).fetchByTitle("Inception");
        verify(movieRepository).save(movie);
        verifyNoMoreInteractions(omdbClient, movieRepository);
    }

    @Test
    void enrichMovieWithRating_whenResponseFalse_shouldNotSetRatingButStillSave() {
        Movie movie = new Movie();
        movie.setTitle("Unknown");

        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("False");

        when(omdbClient.fetchByTitle("Unknown")).thenReturn(response);

        omdbService.enrichMovieWithRating(movie);

        assertEquals(null, movie.getRating());

        verify(omdbClient).fetchByTitle("Unknown");
        verify(movieRepository).save(movie);
        verifyNoMoreInteractions(omdbClient, movieRepository);
    }
}