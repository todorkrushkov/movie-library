package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import telerik.project.movielibrary.helpers.mappers.MovieMapper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.dtos.movie.MovieCreateDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieResponseDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieUpdateDTO;
import telerik.project.movielibrary.services.contracts.MovieService;
import telerik.project.movielibrary.swagger.SecuredApi;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie catalog operations")
@SecuredApi
public class MovieRestController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Operation(summary = "Get all movies")
    @PreAuthorize("@validateAuth.isAuthenticated(authentication)")
    @GetMapping
    public List<MovieResponseDTO> getAll() {
        return movieService.getAll().stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get movie by ID")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @PreAuthorize("@validateAuth.isAuthenticated(authentication)")
    @GetMapping("/id/{targetMovieId}")
    public MovieResponseDTO getById(@PathVariable Long targetMovieId) {
            Movie movie = movieService.getById(targetMovieId);
            return movieMapper.toResponse(movie);
    }

    @Operation(summary = "Get movie by Title")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @PreAuthorize("@validateAuth.isAuthenticated(authentication)")
    @GetMapping("/title/{targetMovieTitle}")
    public MovieResponseDTO getByTitle(@PathVariable String targetMovieTitle) {
        Movie movie = movieService.getByTitle(targetMovieTitle);
        return movieMapper.toResponse(movie);
    }

    @Operation(summary = "Create movie")
    @ApiResponse(responseCode = "409", description = "Movie with the same title already exists")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PostMapping
    public void create(@RequestBody MovieCreateDTO dto) {
            Movie movie = movieMapper.toCreate(dto);
            movieService.create(movie);
    }

    @Operation(summary = "Update movie")
    @ApiResponse(responseCode = "404", description = "Movie not found")
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

    @Operation(summary = "Delete movie")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @DeleteMapping("/{targetMovieId}")
    public void delete(@PathVariable Long targetMovieId) {
            movieService.delete(targetMovieId);
    }
}
