package telerik.project.movielibrary.helpers.mappers;

import org.junit.jupiter.api.Test;
import telerik.project.movielibrary.external.OmdbMovieResponse;
import telerik.project.movielibrary.models.Movie;

import static org.junit.jupiter.api.Assertions.*;

class OmdbMapperTests {

    private final OmdbMapper mapper = new OmdbMapper();

    @Test
    void applyRating_whenResponseTrue_shouldSetRating() {
        Movie movie = new Movie();

        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("True");
        response.setImdbRating(7.5);

        mapper.applyRating(movie, response);

        assertEquals(7.5, movie.getRating());
    }

    @Test
    void applyRating_whenResponseLowercaseTrue_shouldSetRating() {
        Movie movie = new Movie();

        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("true");
        response.setImdbRating(9.1);

        mapper.applyRating(movie, response);

        assertEquals(9.1, movie.getRating());
    }

    @Test
    void applyRating_whenResponseFalse_shouldNotSetRating() {
        Movie movie = new Movie();
        movie.setRating(5.0);

        OmdbMovieResponse response = new OmdbMovieResponse();
        response.setResponse("False");
        response.setImdbRating(9.9);

        mapper.applyRating(movie, response);

        assertEquals(5.0, movie.getRating());
    }
}