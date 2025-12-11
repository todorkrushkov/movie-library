package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getAll();

    Movie getById(Long targetMovieId);

    Movie getByTitle(String title);

    void create(Movie movie);

    void update(Long targetMovieId, Movie updatedMovie);

    void delete(Long targetMovieId);

}
