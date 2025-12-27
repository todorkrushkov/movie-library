package telerik.project.movielibrary.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.services.contracts.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponseDTO<?> login(
            @RequestBody LoginRequestDTO dto,
            HttpServletResponse response
    ) {
        UserResponseDTO user = authService.login(dto, response);
        return ApiResponseDTO.success(200, "Logged in successfully.", user);
    }

    @PostMapping("/logout")
    public ApiResponseDTO<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ApiResponseDTO.success(200, "Logged out successfully.", null);
    }
}
