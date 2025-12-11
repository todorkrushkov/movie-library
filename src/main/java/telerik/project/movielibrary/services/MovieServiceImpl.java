package telerik.project.movielibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.repositories.MovieRepository;
import telerik.project.movielibrary.services.contracts.MovieService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getById(Long targetMovieId) {
        return movieRepository.findById(targetMovieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie", targetMovieId));
    }

    @Override
    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Movie", "title", title));
    }

    @Override
    @Transactional
    public void create(Movie movie) {
        String title = movie.getTitle();

        if (movieRepository.existsByTitle(title)) {
            throw new EntityDuplicateException("Movie", "title", title);
        }

        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void update(Long targetMovieId, Movie updatedMovie) {
        Movie targetMovie = getById(targetMovieId);
        String updatedTitle = updatedMovie.getTitle();

        if (movieRepository.existsByTitle(updatedTitle)) {
            throw new EntityDuplicateException("Movie", "title", updatedTitle);
        }

        targetMovie.setTitle(updatedTitle);
        targetMovie.setDirector(updatedMovie.getDirector());
        targetMovie.setReleaseYear(updatedMovie.getReleaseYear());

        movieRepository.save(targetMovie);
    }

    @Override
    @Transactional
    public void delete(Long targetMovieId) {
        Movie targetMovie = getById(targetMovieId);
        movieRepository.delete(targetMovie);
    }
}
