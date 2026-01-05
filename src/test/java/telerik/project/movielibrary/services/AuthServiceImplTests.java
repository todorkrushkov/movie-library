package telerik.project.movielibrary.services;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.auth.LoginRequestDTO;
import telerik.project.movielibrary.models.dtos.auth.RegisterRequestDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.security.CustomUserDetails;
import telerik.project.movielibrary.security.jwt.JwtCookieUtil;
import telerik.project.movielibrary.security.jwt.JwtUtil;
import telerik.project.movielibrary.services.contracts.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtCookieUtil jwtCookieUtil;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldCreateUserGenerateJwtAndCookie() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("user");
        dto.setPassword("pass12");

        User created = new User();
        created.setId(1L);
        created.setUsername("user");

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userService).create(any(User.class));

        when(userService.getById(1L)).thenReturn(created);

        when(jwtUtil.generateToken(any(CustomUserDetails.class)))
                .thenReturn("jwt-token");

        UserResponseDTO result = authService.register(dto, response);

        assertEquals(1L, result.getId());
        assertEquals("user", result.getUsername());

        verify(userService).create(any(User.class));
        verify(userService).getById(1L);
        verify(jwtUtil).generateToken(any(CustomUserDetails.class));
        verify(jwtCookieUtil).addTokenCookie(response, "jwt-token");
    }

    @Test
    void login_whenCredentialsValid_shouldAuthenticateGenerateJwtAndSetCookie() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("admin");
        dto.setPassword("pass");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

        UserResponseDTO result = authService.login(dto, response);

        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(userDetails);
        verify(jwtCookieUtil).addTokenCookie(response, "jwt-token");
    }

    @Test
    void logout_shouldClearJwtCookie() {
        authService.logout(response);

        verify(jwtCookieUtil).clearTokenCookie(response);
        verifyNoMoreInteractions(jwtCookieUtil, authenticationManager, userService);
    }
}
