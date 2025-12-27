package telerik.project.movielibrary.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.security.CustomUserDetails;
import telerik.project.movielibrary.security.jwt.JwtUtil;
import telerik.project.movielibrary.services.contracts.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        response.setHeader("Set-Cookie", String.format(
                        "%s=%s; " +
                        "Max-Age=%d; " +
                        "Path=/; " +
                        "HttpOnly; " +
                        "SameSite=Strict",
                cookie.getName(),
                cookie.getValue(),
                cookie.getMaxAge()
        ));

        return new UserResponseDTO(userDetails.getId(), userDetails.getUsername(), userDetails.getRole().name());
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        response.setHeader("Set-Cookie", String.format(
                "%s=%s; " +
                        "Max-Age=%d; " +
                        "Path=/; " +
                        "HttpOnly; " +
                        "SameSite=Strict",
                cookie.getName(),
                cookie.getValue(),
                0
        ));
    }
}
