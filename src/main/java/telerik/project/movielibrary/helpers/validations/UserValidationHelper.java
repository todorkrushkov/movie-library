package telerik.project.movielibrary.helpers.validations;

import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.repositories.UserRepository;

public final class UserValidationHelper {

    private UserValidationHelper() {}

    public static void validateUsernameUpdate(
            UserRepository userRepository, String newUsername, String oldUsername) {
        if (newUsername == null || newUsername.isBlank()) {
            return;
        }

        if (newUsername.equals(oldUsername)) {
            return;
        }

        validateUsernameNotTaken(userRepository, newUsername);
    }

    public static void validateUsernameNotTaken(UserRepository userRepository, String username) {
        if (userRepository.existsByUsername(username)) {
            throw new EntityDuplicateException("User", "username", username);
        }
    }

}
