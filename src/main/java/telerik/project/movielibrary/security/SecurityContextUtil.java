package telerik.project.movielibrary.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        if (!(auth.getPrincipal() instanceof CustomUserDetails principal)) {
            return null;
        }

        return principal.getId();
    }
}
