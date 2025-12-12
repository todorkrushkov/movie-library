package telerik.project.movielibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import telerik.project.movielibrary.external.OmdbClient;
import telerik.project.movielibrary.external.OmdbMovieResponse;
import telerik.project.movielibrary.helpers.mappers.OmdbMapper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.repositories.MovieRepository;
import telerik.project.movielibrary.services.contracts.OmdbService;

@Service
@RequiredArgsConstructor
public class OmdbServiceImpl implements OmdbService {

    private final OmdbClient omdbClient;
    private final OmdbMapper omdbMapper;
    private final MovieRepository movieRepository;

    @Override
    @Async
    public void enrichMovieWithRating(Movie movie) {
        OmdbMovieResponse response = omdbClient.fetchByTitle(movie.getTitle());

        omdbMapper.applyRating(movie, response);
        movieRepository.save(movie);
    }
}
