package telerik.project.movielibrary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.helpers.mappers.MovieMapper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.dtos.movie.MovieCreateDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieResponseDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieUpdateDTO;
import telerik.project.movielibrary.services.contracts.MovieService;

import java.util.List;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
public class MovieRestController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieResponseDTO> getAll() {
        return movieService.getAll().stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @GetMapping("/{targetMovieId}")
    public MovieResponseDTO getById(@PathVariable Long targetMovieId) {
        try {
            Movie movie = movieService.getById(targetMovieId);
            return movieMapper.toResponse(movie);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void create(@RequestBody MovieCreateDTO dto) {
        try {
            Movie movie = movieMapper.toCreate(dto);
            movieService.create(movie);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{targetMovieId}")
    public void update(
            @PathVariable Long targetMovieId,
            @RequestBody MovieUpdateDTO dto
            ) {
        try {
            Movie targetMovie = movieService.getById(targetMovieId);
            movieMapper.toUpdate(targetMovie, dto);
            movieService.update(targetMovieId, targetMovie);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{targetMovieId}")
    public void delete(@PathVariable Long targetMovieId) {
        try {
            movieService.delete(targetMovieId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
