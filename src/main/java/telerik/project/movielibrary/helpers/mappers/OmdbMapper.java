package telerik.project.movielibrary.helpers.mappers;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.external.OmdbMovieResponse;
import telerik.project.movielibrary.models.Movie;

@Component
public class OmdbMapper {

    public void applyRating(Movie movie, OmdbMovieResponse omdb) {
        if ("True".equalsIgnoreCase(omdb.getResponse())) {
            movie.setRating(omdb.getImdbRating());
        }
    }
}
