package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.auth.RegisterRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.services.contracts.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and session management")
public class AuthRestController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "409", description = "Username already exists")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PostMapping("/register")
    public ApiResponseDTO<?> register(
            @Valid @RequestBody RegisterRequestDTO dto,
            HttpServletResponse response
    ) {
        UserResponseDTO user = authService.register(dto, response);
        return ApiResponseDTO.success(200, "Registered successfully.", user);
    }

    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "Login successfully")
    @ApiResponse(responseCode = "409", description = "Invalid username or password")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PostMapping("/login")
    public ApiResponseDTO<?> login(
            @Valid @RequestBody LoginRequestDTO dto,
            HttpServletResponse response
    ) {
        UserResponseDTO user = authService.login(dto, response);
        return ApiResponseDTO.success(200, "Logged in successfully.", user);
    }

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @PostMapping("/logout")
    public ApiResponseDTO<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ApiResponseDTO.success(200, "Logged out successfully.", null);
    }
}
