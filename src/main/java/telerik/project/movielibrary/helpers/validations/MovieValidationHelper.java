package telerik.project.movielibrary.helpers.validations;

import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.repositories.MovieRepository;

public final class MovieValidationHelper {

    private MovieValidationHelper() {}

    public static void validateTitleUpdate(MovieRepository movieRepository, String newTitle, String oldTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            return;
        }

        if (newTitle.equals(oldTitle)) {
            return;
        }

        validateTitleNotTaken(movieRepository, newTitle);
    }

    public static void validateTitleNotTaken(MovieRepository movieRepository, String title) {
        if (movieRepository.existsByTitle(title)) {
            throw new EntityDuplicateException("Movie", "title", title);
        }
    }

}
