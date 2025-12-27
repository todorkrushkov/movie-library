package telerik.project.movielibrary.services.contracts;

import jakarta.servlet.http.HttpServletResponse;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;

public interface AuthService {

    UserResponseDTO login(LoginRequestDTO request, HttpServletResponse response);

    void logout(HttpServletResponse response);
}
