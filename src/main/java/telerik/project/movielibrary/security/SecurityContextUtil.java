package telerik.project.movielibrary.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import telerik.project.movielibrary.models.Role;

@Component("securityUtil")
public class SecurityContextUtil {
    public boolean canModify(Long targetUserId, Authentication authentication) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return user.getRole() == Role.ADMIN
                || user.getId().equals(targetUserId);
    }
}
