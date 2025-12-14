package telerik.project.movielibrary.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        service = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("pass");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("pass", result.getPassword());
        assertTrue(result.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository).findByUsername("admin");
    }

    @Test
    void loadUserByUsername_whenUserMissing_shouldThrowUsernameNotFoundException() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing"));

        verify(userRepository).findByUsername("missing");
    }
}