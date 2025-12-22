package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll(String username, Role role);

    User getById(Long targetUserId);

    void create(User user);

    void update(Long targetUserId, User updatedUser);

    void delete(Long targetUserId);

}
