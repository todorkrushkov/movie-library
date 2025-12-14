package telerik.project.movielibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
import telerik.project.movielibrary.helpers.validations.UserValidationHelper;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.repositories.UserRepository;
import telerik.project.movielibrary.services.contracts.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long targetUserId) {
        return userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", targetUserId));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    @Transactional
    public void create(User user) {
        UserValidationHelper.validateUsernameNotTaken(userRepository, user.getUsername());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(Long targetUserId, User updatedUser) {
        User targetUser = getById(targetUserId);
        String updatedUsername = updatedUser.getUsername();

        UserValidationHelper.validateUsernameUpdate(userRepository, updatedUsername, targetUser.getUsername());

        targetUser.setUsername(updatedUsername);
        targetUser.setPassword(updatedUser.getPassword());

        userRepository.save(targetUser);
    }

    @Override
    @Transactional
    public void delete(Long targetUserId) {
        User targetUser = getById(targetUserId);
        userRepository.delete(targetUser);
    }
}
