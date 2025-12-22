package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getAll(
            String title,
            String director,
            Integer yearFrom,
            Integer yearTo,
            Double ratingMin,
            Double ratingMax
    );

    Movie getById(Long targetMovieId);

    void create(Movie movie);

    void update(Long targetMovieId, Movie updatedMovie);

    void delete(Long targetMovieId);

}
