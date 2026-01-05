package telerik.project.movielibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.helpers.validations.MovieValidationHelper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.repositories.MovieRepository;
import telerik.project.movielibrary.services.contracts.MovieService;
import telerik.project.movielibrary.services.contracts.OmdbService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final OmdbService omdbService;

    @Override
    public List<Movie> getAll(
            String title,
            String director,
            LocalDate dateFrom,
            LocalDate dateTo,
            Double ratingMin,
            Double ratingMax
    ) {
        return movieRepository.search(title, director, dateFrom, dateTo, ratingMin, ratingMax);
    }

    @Override
    public Movie getById(Long targetMovieId) {
        return movieRepository.findById(targetMovieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie", targetMovieId));
    }

    @Override
    @Transactional
    public void create(Movie movie) {
        MovieValidationHelper.validateTitleNotTaken(movieRepository, movie.getTitle());
        movieRepository.save(movie);
        omdbService.enrichMovie(movie);
    }

    @Override
    @Transactional
    public void update(Long targetMovieId, Movie updatedMovie) {
        Movie targetMovie = getById(targetMovieId);
        String updatedTitle = updatedMovie.getTitle();

        MovieValidationHelper.validateTitleUpdate(movieRepository, updatedTitle, targetMovie.getTitle());

        targetMovie.setTitle(updatedTitle);
        movieRepository.save(targetMovie);
        omdbService.enrichMovie(targetMovie);
    }

    @Override
    @Transactional
    public void delete(Long targetMovieId) {
        Movie targetMovie = getById(targetMovieId);
        movieRepository.delete(targetMovie);
    }
}
