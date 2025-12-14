package telerik.project.movielibrary.helpers.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import telerik.project.movielibrary.exceptions.AuthenticationFailureException;
import telerik.project.movielibrary.exceptions.AuthorizationFailureException;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.security.CustomUserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoBean(name = "validateAuth")
class AuthValidationHelperTests {

    private AuthValidationHelper authHelper;

    @Mock
    private Authentication authentication;

    private CustomUserDetails adminUser;
    private CustomUserDetails normalUser;

    @BeforeEach
    void setUp() {
        authHelper = new AuthValidationHelper();

        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("x");
        admin.setRole(Role.ADMIN);

        User user = new User();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword("y");
        user.setRole(Role.USER);

        adminUser = new CustomUserDetails(admin);
        normalUser = new CustomUserDetails(user);
    }

    @Test
    void isAuthenticated_whenAuthenticationNull_shouldThrowAuthenticationFailureException() {
        assertThrows(AuthenticationFailureException.class,
                () -> authHelper.isAuthenticated(null));
    }

    @Test
    void isAuthenticated_whenNotAuthenticated_shouldThrowAuthenticationFailureException() {
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(AuthenticationFailureException.class,
                () -> authHelper.isAuthenticated(authentication));
    }

    @Test
    void isAuthenticated_whenPrincipalInvalid_shouldThrowAuthenticationFailureException() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not-a-user");

        assertThrows(AuthenticationFailureException.class,
                () -> authHelper.isAuthenticated(authentication));
    }

    @Test
    void isAuthenticated_whenValid_shouldReturnTrue() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(normalUser);

        assertTrue(authHelper.isAuthenticated(authentication));
    }

    @Test
    void isAdmin_whenUserIsAdmin_shouldReturnTrue() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(adminUser);

        assertTrue(authHelper.isAdmin(authentication));
    }

    @Test
    void isAdmin_whenUserIsNotAdmin_shouldThrowAuthorizationFailureException() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(normalUser);

        assertThrows(AuthorizationFailureException.class,
                () -> authHelper.isAdmin(authentication));
    }

    @Test
    void isOwnerOrAdmin_whenAdmin_shouldReturnTrue() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(adminUser);

        assertTrue(authHelper.isOwnerOrAdmin(999L, authentication));
    }

    @Test
    void isOwnerOrAdmin_whenOwner_shouldReturnTrue() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(normalUser);

        assertTrue(authHelper.isOwnerOrAdmin(2L, authentication));
    }

    @Test
    void isOwnerOrAdmin_whenNotOwnerAndNotAdmin_shouldThrowAuthorizationFailureException() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(normalUser);

        assertThrows(AuthorizationFailureException.class,
                () -> authHelper.isOwnerOrAdmin(999L, authentication));
    }
}