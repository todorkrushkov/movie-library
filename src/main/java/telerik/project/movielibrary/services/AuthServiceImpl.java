package telerik.project.movielibrary.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.auth.RegisterRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.security.CustomUserDetails;
import telerik.project.movielibrary.security.jwt.JwtCookieUtil;
import telerik.project.movielibrary.security.jwt.JwtUtil;
import telerik.project.movielibrary.services.contracts.AuthService;
import telerik.project.movielibrary.services.contracts.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtCookieUtil jwtCookieUtil;
    private final UserService userService;

    public UserResponseDTO register(RegisterRequestDTO request, HttpServletResponse response) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        userService.create(user);

        User created = userService.getById(user.getId());

        CustomUserDetails userDetails = new CustomUserDetails(created);

        String jwt = jwtUtil.generateToken(userDetails);
        jwtCookieUtil.addTokenCookie(response, jwt);

        return new UserResponseDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getRole().name());
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        jwtCookieUtil.addTokenCookie(response, jwt);

        return new UserResponseDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getRole().name());
    }

    @Override
    public void logout(HttpServletResponse response) {
        jwtCookieUtil.clearTokenCookie(response);
    }
}
