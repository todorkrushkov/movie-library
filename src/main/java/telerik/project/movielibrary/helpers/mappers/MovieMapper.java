package telerik.project.movielibrary.helpers.mappers;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.dtos.movie.MovieCreateDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieResponseDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieUpdateDTO;

@Component
public class MovieMapper {

    public MovieResponseDTO toResponse(Movie movie) {
        MovieResponseDTO dto = new MovieResponseDTO();

        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDirector(movie.getDirector());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setRating(movie.getRating());

        return dto;
    }

    public void toUpdate(Movie movie , MovieUpdateDTO dto) {
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            movie.setTitle(dto.getTitle());
        }
    }

    public Movie toCreate(MovieCreateDTO dto) {
        Movie movie = new Movie();

        movie.setTitle(dto.getTitle());

        return movie;
    }
}
