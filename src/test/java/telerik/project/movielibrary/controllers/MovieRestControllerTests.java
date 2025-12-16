package telerik.project.movielibrary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import telerik.project.movielibrary.config.SecurityTestConfig;
import telerik.project.movielibrary.exceptions.AuthorizationFailureException;
import telerik.project.movielibrary.exceptions.GlobalExceptionHandler;
import telerik.project.movielibrary.helpers.mappers.MovieMapper;
import telerik.project.movielibrary.helpers.validations.AuthValidationHelper;
import telerik.project.movielibrary.models.Movie;
import telerik.project.movielibrary.models.dtos.movie.MovieCreateDTO;
import telerik.project.movielibrary.models.dtos.movie.MovieUpdateDTO;
import telerik.project.movielibrary.services.contracts.MovieService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieRestController.class)
@Import({
        SecurityTestConfig.class,
        MovieMapper.class,
        GlobalExceptionHandler.class
})
class MovieRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private MovieService movieService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean(name = "validateAuth")
    private AuthValidationHelper authValidationHelper;

    @BeforeEach
    void setupAuthDefaults() {
        when(authValidationHelper.isAuthenticated(any())).thenReturn(true);
        when(authValidationHelper.isAdmin(any())).thenReturn(true);
    }

    @Test
    void getAll_whenAuthenticated_shouldReturn200() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Interstellar");

        when(movieService.getAll()).thenReturn(java.util.List.of(movie));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Interstellar"));

        verify(movieService).getAll();
    }

    @Test
    void getById_whenAuthenticated_shouldReturn200() throws Exception {
        Movie movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Inception");

        when(movieService.getById(10L)).thenReturn(movie);

        mockMvc.perform(get("/api/movies/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));

        verify(movieService).getById(10L);
    }

    @Test
    void create_whenAdmin_shouldReturn200() throws Exception {
        MovieCreateDTO dto = new MovieCreateDTO();
        dto.setTitle("New Movie");
        dto.setDirector("Someone");
        dto.setReleaseYear(2024);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(movieService).create(any(Movie.class));
    }

    @Test
    void create_whenUser_shouldReturn403() throws Exception {
        when(authValidationHelper.isAdmin(any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        MovieCreateDTO dto = new MovieCreateDTO();
        dto.setTitle("title");

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(movieService, never()).create(any());
    }

    @Test
    void update_whenAdmin_shouldReturn200() throws Exception {
        Movie movie = new Movie();
        movie.setId(5L);

        when(movieService.getById(5L)).thenReturn(movie);

        MovieUpdateDTO dto = new MovieUpdateDTO();
        dto.setTitle("Updated");

        mockMvc.perform(put("/api/movies/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(movieService).update(eq(5L), any(Movie.class));
    }

    @Test
    void update_whenUser_shouldReturn403() throws Exception {
        when(authValidationHelper.isAdmin(any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        MovieUpdateDTO dto = new MovieUpdateDTO();
        dto.setTitle("newTitle");

        mockMvc.perform(put("/api/movies/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(movieService, never()).update(anyLong(), any());
    }

    @Test
    void delete_whenAdmin_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/movies/5"))
                .andExpect(status().isOk());

        verify(movieService).delete(5L);
    }

    @Test
    void delete_whenUser_shouldReturn403() throws Exception {
        when(authValidationHelper.isAdmin(any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        mockMvc.perform(delete("/api/movies/5"))
                .andExpect(status().isForbidden());

        verify(movieService, never()).delete(anyLong());
    }
}