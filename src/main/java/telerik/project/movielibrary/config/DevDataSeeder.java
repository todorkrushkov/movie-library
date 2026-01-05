package telerik.project.movielibrary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.services.contracts.UserService;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSeeder implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin12");
            admin.setRole(Role.ADMIN);

            userService.create(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword("12345678");
            user.setRole(Role.USER);

            userService.create(user);
        }
    }
}