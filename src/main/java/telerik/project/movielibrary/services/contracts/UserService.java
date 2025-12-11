package telerik.project.movielibrary.services.contracts;

import telerik.project.movielibrary.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long targetUserId);

    User getByUsername(String username);

    void create(User user);

    void update(Long targetUserId, User updatedUser);

    void delete(Long targetUserId);

}
