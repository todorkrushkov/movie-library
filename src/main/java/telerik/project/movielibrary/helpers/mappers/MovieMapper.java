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
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setRating(movie.getRating());

        return dto;
    }

    public void toUpdate(Movie movie , MovieUpdateDTO dto) {
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            movie.setTitle(dto.getTitle());
        }

        if (dto.getDirector() != null && !dto.getDirector().isBlank()) {
            movie.setDirector(dto.getDirector());
        }

        if (dto.getReleaseYear() != null) {
            movie.setReleaseYear(dto.getReleaseYear());
        }
    }

    public Movie toCreate(MovieCreateDTO dto) {
        Movie movie = new Movie();

        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());

        return movie;
    }

}
