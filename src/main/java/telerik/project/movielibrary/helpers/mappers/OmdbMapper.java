package telerik.project.movielibrary.helpers.mappers;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.external.OmdbMovieResponse;
import telerik.project.movielibrary.models.Movie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class OmdbMapper {

    private static final DateTimeFormatter OMDB_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

    public void applyOmdbData(Movie movie, OmdbMovieResponse omdb) {
        if (omdb == null || !"True".equalsIgnoreCase(omdb.getResponse())) {
            return;
        }

        if (omdb.getImdbRating() != null) {
            movie.setRating(omdb.getImdbRating());
        }

        if (omdb.getDirector() != null && !"N/A".equalsIgnoreCase(omdb.getDirector())) {
            movie.setDirector(omdb.getDirector());
        }

        if (omdb.getReleased() != null && !"N/A".equalsIgnoreCase(omdb.getReleased())) {
                LocalDate parsedDate = LocalDate.parse(omdb.getReleased(), OMDB_DATE_FORMAT);
                movie.setReleaseDate(parsedDate);
        }
    }
}
