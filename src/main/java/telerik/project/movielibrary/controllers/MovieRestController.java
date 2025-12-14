package telerik.project.movielibrary.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PreAuthorize("@validateAuth.isAuthenticated(authentication)")
    @GetMapping
    public List<MovieResponseDTO> getAll() {
        return movieService.getAll().stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @PreAuthorize("@validateAuth.isAuthenticated(authentication)")
    @GetMapping("/{targetMovieId}")
    public MovieResponseDTO getById(@PathVariable Long targetMovieId) {
            Movie movie = movieService.getById(targetMovieId);
            return movieMapper.toResponse(movie);
    }

    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PostMapping
    public void create(@RequestBody MovieCreateDTO dto) {
            Movie movie = movieMapper.toCreate(dto);
            movieService.create(movie);
    }

    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PutMapping("/{targetMovieId}")
    public void update(
            @PathVariable Long targetMovieId,
            @RequestBody MovieUpdateDTO dto
            ) {
            Movie targetMovie = movieService.getById(targetMovieId);
            movieMapper.toUpdate(targetMovie, dto);
            movieService.update(targetMovieId, targetMovie);
    }

    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @DeleteMapping("/{targetMovieId}")
    public void delete(@PathVariable Long targetMovieId) {
            movieService.delete(targetMovieId);
    }
}
