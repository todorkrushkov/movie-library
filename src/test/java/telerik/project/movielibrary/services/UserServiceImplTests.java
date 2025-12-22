package telerik.project.movielibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldname");
        existingUser.setPassword("oldpass");
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        List<User> result = userService.getAll(isNull(), isNull());

        assertEquals(1, result.size());
        assertSame(existingUser, result.get(0));
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getById_whenExists_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        User result = userService.getById(1L);

        assertSame(existingUser, result);
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getById_whenMissing_shouldThrowThrowEntityNotFoundException() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getById(42L));

        verify(userRepository).findById(42L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void create_whenUsernameFree_shouldCheckExistsAndSave() {
        User newUser = new User();
        newUser.setUsername("newname");
        newUser.setPassword("p");

        when(userRepository.existsByUsername("newname")).thenReturn(false);

        userService.create(newUser);

        verify(userRepository).existsByUsername("newname");
        verify(userRepository).save(newUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void create_whenUsernameTaken_shouldThrowEntityDuplicateException() {
        User newUser = new User();
        newUser.setUsername("taken");
        newUser.setPassword("p");

        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThrows(EntityDuplicateException.class, () -> userService.create(newUser));

        verify(userRepository).existsByUsername("taken");
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_whenTargetMissing_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User updated = new User();
        updated.setUsername("whatever");
        updated.setPassword("pass");

        assertThrows(EntityNotFoundException.class, () -> userService.update(99L, updated));

        verify(userRepository).findById(99L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_whenUsernameChangedAndFree_shouldCheckExistsAndSave() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newname")).thenReturn(false);

        User updated = new User();
        updated.setUsername("newname");
        updated.setPassword("newpass");

        userService.update(1L, updated);

        assertEquals("newname", existingUser.getUsername());
        assertEquals("newpass", existingUser.getPassword());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("newname");
        verify(userRepository).save(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_whenUsernameChangedButTaken_shouldThrowEntityDuplicateException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        User updated = new User();
        updated.setUsername("taken");
        updated.setPassword("newpass");

        assertThrows(EntityDuplicateException.class, () -> userService.update(1L, updated));

        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("taken");
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.delete(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void delete_whenMissing_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(123L));

        verify(userRepository).findById(123L);
        verify(userRepository, never()).delete(any());
        verifyNoMoreInteractions(userRepository);
    }
}