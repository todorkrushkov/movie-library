package telerik.project.movielibrary.helpers.validations;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import telerik.project.movielibrary.exceptions.AuthenticationFailureException;
import telerik.project.movielibrary.exceptions.AuthorizationFailureException;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.security.CustomUserDetails;

@Component("validateAuth")
public class AuthValidationHelper {

    public boolean isAuthenticated(Authentication authentication) {
        extractUser(authentication);
        return true;
    }

    public boolean isAdmin(Authentication authentication) {
        CustomUserDetails user = extractUser(authentication);

        if (user.getRole() != Role.ADMIN) {
            throw new AuthorizationFailureException("Only admins can perform this action.");
        }

        return true;
    }

    public boolean isOwnerOrAdmin(Long targetUserId, Authentication authentication) {
        CustomUserDetails user = extractUser(authentication);

        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        if (!user.getId().equals(targetUserId)) {
            throw new AuthorizationFailureException("You are not allowed to perform this action.");
        }

        return true;
    }

    private CustomUserDetails extractUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationFailureException("Authentication required.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails user)) {
            throw new AuthenticationFailureException("Invalid authentication principal.");
        }

        return user;
    }
}
