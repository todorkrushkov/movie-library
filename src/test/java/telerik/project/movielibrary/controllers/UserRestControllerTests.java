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
import telerik.project.movielibrary.helpers.mappers.UserMapper;
import telerik.project.movielibrary.helpers.validations.AuthValidationHelper;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.user.UserCreateDTO;
import telerik.project.movielibrary.models.dtos.user.UserUpdateDTO;
import telerik.project.movielibrary.services.contracts.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@Import({
        SecurityTestConfig.class,
        UserMapper.class,
        GlobalExceptionHandler.class
})
class UserRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private UserService userService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean(name = "validateAuth")
    private AuthValidationHelper authValidationHelper;

    @BeforeEach
    void setupAuthDefaults() {
        when(authValidationHelper.isAuthenticated(any())).thenReturn(true);
        when(authValidationHelper.isAdmin(any())).thenReturn(true);
        when(authValidationHelper.isOwnerOrAdmin(anyLong(), any())).thenReturn(true);
    }

    @Test
    void getAll_whenAdmin_shouldReturn200() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

        when(userService.getAll(
                isNull(),
                isNull()
        )).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));

        verify(userService).getAll(null, null);
    }

    @Test
    void getAll_whenUser_shouldReturn403() throws Exception {
        when(authValidationHelper.isAdmin(any()))
                .thenThrow(new AuthorizationFailureException("Only admins"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAll(eq("maria"), null);
    }

    @Test
    void getById_whenOwner_shouldReturn200() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setUsername("me");

        when(userService.getById(5L)).thenReturn(user);

        mockMvc.perform(get("/api/users/id/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("me"));

        verify(userService).getById(5L);
    }

    @Test
    void getById_whenNotOwner_shouldReturn403() throws Exception {
        when(authValidationHelper.isOwnerOrAdmin(eq(5L), any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        mockMvc.perform(get("/api/users/id/5"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getById(anyLong());
    }

    @Test
    void create_whenAdmin_shouldReturn200() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("newUser");
        dto.setPassword("pass");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"));

        verify(userService).create(any(User.class));
    }

    @Test
    void create_whenUser_shouldReturn403() throws Exception {
        when(authValidationHelper.isAdmin(any()))
                .thenThrow(new AuthorizationFailureException("Only admins"));

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("user");
        dto.setPassword("pass");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(userService, never()).create(any());
    }

    @Test
    void update_whenOwner_shouldReturn200() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setUsername("old");

        when(userService.getById(5L)).thenReturn(user);

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("new");

        mockMvc.perform(put("/api/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new"));

        verify(userService).update(eq(5L), any(User.class));
    }

    @Test
    void update_whenNotOwner_shouldReturn403() throws Exception {
        when(authValidationHelper.isOwnerOrAdmin(eq(5L), any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("newName");

        mockMvc.perform(put("/api/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(userService, never()).update(anyLong(), any());
    }

    @Test
    void delete_whenOwner_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/users/5"))
                .andExpect(status().isOk());

        verify(userService).delete(5L);
    }

    @Test
    void delete_whenNotOwner_shouldReturn403() throws Exception {
        when(authValidationHelper.isOwnerOrAdmin(eq(5L), any()))
                .thenThrow(new AuthorizationFailureException("Forbidden"));

        mockMvc.perform(delete("/api/users/5"))
                .andExpect(status().isForbidden());

        verify(userService, never()).delete(anyLong());
    }
}