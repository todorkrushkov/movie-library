package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import telerik.project.movielibrary.helpers.mappers.WatchedMovieMapper;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;
import telerik.project.movielibrary.models.dtos.watchedmovie.WatchedMovieNoteDTO;
import telerik.project.movielibrary.models.dtos.watchedmovie.WatchedMovieResponseDTO;
import telerik.project.movielibrary.services.contracts.WatchedMovieService;
import telerik.project.movielibrary.swagger.SecuredApi;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/watched")
@RequiredArgsConstructor
@Tag(name = "Watched Movies", description = "Watched movies per user")
@SecuredApi
public class WatchedMovieRestController {

    private final WatchedMovieService watchedMovieService;
    private final WatchedMovieMapper watchedMovieMapper;

    @Operation(summary = "Get watched movies for user")
    @ApiResponse(responseCode = "200", description = "Watched movies retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @authorize.isOwner(#userId)")
    public ApiResponseDTO<List<WatchedMovieResponseDTO>> getWatchedMovies(@PathVariable Long userId) {
        List<WatchedMovieResponseDTO> result = watchedMovieService
                .getWatchedMovies(userId)
                .stream()
                .map(watchedMovieMapper::toResponse)
                .toList();

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Watched movies retrieved successfully.",
                result
        );
    }

    @Operation(summary = "Add movie to watched list")
    @ApiResponse(responseCode = "201", description = "Movie added to watched list")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PostMapping("/{movieId}")
    @PreAuthorize("@authorize.isOwner(#userId)")
    public ApiResponseDTO<Void> addWatchedMovie(
            @PathVariable Long userId,
            @PathVariable Long movieId,
            @Valid @RequestBody WatchedMovieNoteDTO dto
    ) {
        watchedMovieService.addWatchedMovie(userId, movieId, dto.getNote());

        return ApiResponseDTO.success(
                HttpStatus.CREATED.value(),
                "Movie added to watched list.",
                null
        );
    }

    @Operation(summary = "Update watched movie note")
    @ApiResponse(responseCode = "200", description = "Note updated successfully")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PutMapping("/{movieId}")
    @PreAuthorize("@authorize.isOwner(#userId)")
    public ApiResponseDTO<Void> updateNote(
            @PathVariable Long userId,
            @PathVariable Long movieId,
            @Valid @RequestBody WatchedMovieNoteDTO dto
    ) {
        watchedMovieService.updateNote(userId, movieId, dto.getNote());

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Note updated successfully.",
                null
        );
    }
}
