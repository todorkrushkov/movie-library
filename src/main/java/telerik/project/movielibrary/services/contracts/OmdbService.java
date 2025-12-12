package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.Movie;

public interface OmdbService {

    void enrichMovieWithRating(Movie movie);
}
