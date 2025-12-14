package telerik.project.movielibrary.helpers.validations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationHelperTests {

    @Mock
    private UserRepository userRepository;

    @Test
    void validateUsernameNotTaken_whenTaken_shouldThrow() {
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThrows(EntityDuplicateException.class,
                () -> UserValidationHelper.validateUsernameNotTaken(userRepository, "taken"));

        verify(userRepository).existsByUsername("taken");
    }

    @Test
    void validateUsernameNotTaken_whenFree_shouldPass() {
        when(userRepository.existsByUsername("free")).thenReturn(false);

        assertDoesNotThrow(() ->
                UserValidationHelper.validateUsernameNotTaken(userRepository, "free"));

        verify(userRepository).existsByUsername("free");
    }

    @Test
    void validateUsernameUpdate_whenNull_shouldSkip() {
        assertDoesNotThrow(() ->
                UserValidationHelper.validateUsernameUpdate(userRepository, null, "old"));

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUsernameUpdate_whenBlank_shouldSkip() {
        assertDoesNotThrow(() ->
                UserValidationHelper.validateUsernameUpdate(userRepository, "   ", "old"));

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUsernameUpdate_whenSame_shouldSkip() {
        assertDoesNotThrow(() ->
                UserValidationHelper.validateUsernameUpdate(userRepository, "same", "same"));

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUsernameUpdate_whenChangedAndTaken_shouldThrow() {
        when(userRepository.existsByUsername("new")).thenReturn(true);

        assertThrows(EntityDuplicateException.class, () ->
                UserValidationHelper.validateUsernameUpdate(userRepository, "new", "old"));

        verify(userRepository).existsByUsername("new");
    }

    @Test
    void validateUsernameUpdate_whenChangedAndFree_shouldPass() {
        when(userRepository.existsByUsername("new")).thenReturn(false);

        assertDoesNotThrow(() ->
                UserValidationHelper.validateUsernameUpdate(userRepository, "new", "old"));

        verify(userRepository).existsByUsername("new");
    }
}