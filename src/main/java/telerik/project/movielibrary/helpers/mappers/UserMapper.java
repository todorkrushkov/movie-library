package telerik.project.movielibrary.helpers.mappers;

import org.springframework.stereotype.Component;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.models.dtos.user.UserUpdateDTO;

@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());

        return dto;
    }

    public void toUpdate(User user ,UserUpdateDTO dto) {
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }
    }
}
