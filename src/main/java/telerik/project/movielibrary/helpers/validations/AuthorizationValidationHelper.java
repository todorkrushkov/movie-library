package telerik.project.movielibrary.helpers.validations;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.security.SecurityContextUtil;

import java.util.Objects;

@Component("authorize")
public class AuthorizationValidationHelper {
    public boolean isOwner(Long userId) {
        Long currentUserId = SecurityContextUtil.getCurrentUserId();
        return Objects.equals(currentUserId, userId);
    }
}
