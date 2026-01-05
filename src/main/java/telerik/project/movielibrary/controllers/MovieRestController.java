package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import telerik.project.movielibrary.helpers.mappers.MovieMapper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieCreateDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieResponseDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieUpdateDTO;
import telerik.project.movielibrary.services.contracts.MovieService;
import telerik.project.movielibrary.swagger.SecuredApi;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie catalog operations")
@SecuredApi
public class MovieRestController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Operation(summary = "Get all movies with filters")
    @ApiResponse(responseCode = "200", description = "Movies retrieved successfully")
    @GetMapping
    public ApiResponseDTO<List<MovieResponseDTO>> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false) Double ratingMax
    ) {
        List<MovieResponseDTO> movies = movieService
                .getAll(title, director, dateFrom, dateTo, ratingMin, ratingMax)
                .stream()
                .map(movieMapper::toResponse)
                .toList();

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Movies retrieved successfully.",
                movies
        );
    }

    @Operation(summary = "Get movie by ID")
    @ApiResponse(responseCode = "200", description = "Movie retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @GetMapping("/{targetMovieId}")
    public ApiResponseDTO<MovieResponseDTO> getById(@PathVariable Long targetMovieId) {
        Movie movie = movieService.getById(targetMovieId);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                null,
                movieMapper.toResponse(movie)
        );
    }

    @Operation(summary = "Create movie")
    @ApiResponse(responseCode = "201", description = "Movie created successfully")
    @ApiResponse(responseCode = "409", description = "Movie with the same title already exists")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponseDTO<MovieResponseDTO> create(@Valid @RequestBody MovieCreateDTO dto) {
        Movie movie = movieMapper.toCreate(dto);
        movieService.create(movie);

        return ApiResponseDTO.success(
                HttpStatus.CREATED.value(),
                "Movie created successfully.",
                movieMapper.toResponse(movie)
        );
    }

    @Operation(summary = "Update movie")
    @ApiResponse(responseCode = "200", description = "Movie updated successfully")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{targetMovieId}")
    public ApiResponseDTO<MovieResponseDTO> update(
            @PathVariable Long targetMovieId,
            @Valid @RequestBody MovieUpdateDTO dto
    ) {
        Movie targetMovie = movieService.getById(targetMovieId);
        movieMapper.toUpdate(targetMovie, dto);
        movieService.update(targetMovieId, targetMovie);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Movie updated successfully.",
                movieMapper.toResponse(targetMovie)
        );
    }

    @Operation(summary = "Delete movie")
    @ApiResponse(responseCode = "200", description = "Movie deleted successfully")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{targetMovieId}")
    public ApiResponseDTO<Void> delete(@PathVariable Long targetMovieId) {
        movieService.delete(targetMovieId);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Movie deleted successfully.",
                null
        );
    }
}
