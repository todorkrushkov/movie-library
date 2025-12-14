package telerik.project.movielibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import telerik.project.movielibrary.helpers.mappers.UserMapper;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.user.UserCreateDTO;
import telerik.project.movielibrary.models.dtos.user.UserResponseDTO;
import telerik.project.movielibrary.models.dtos.user.UserUpdateDTO;
import telerik.project.movielibrary.services.contracts.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @GetMapping
    public List<UserResponseDTO> getAll() {
        return userService.getAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @GetMapping("/{targetUserId}")
    public UserResponseDTO getById(@PathVariable Long targetUserId) {
        User user = userService.getById(targetUserId);
        return userMapper.toResponse(user);
    }

    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserCreateDTO dto) {
        User user = userMapper.toCreate(dto);
        userService.create(user);
        return userMapper.toResponse(user);
    }

    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @PutMapping("/{targetUserId}")
    public UserResponseDTO update(
            @PathVariable Long targetUserId,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        User targetUser = userService.getById(targetUserId);
        userMapper.toUpdate(targetUser, dto);
        userService.update(targetUserId, targetUser);
        return userMapper.toResponse(targetUser);
    }

    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @DeleteMapping("/{targetUserId}")
    public void delete(@PathVariable Long targetUserId) {
        userService.delete(targetUserId);
    }
}
