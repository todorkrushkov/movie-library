package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import telerik.project.movielibrary.swagger.SecuredApi;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management and profiles")
@SecuredApi
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get all users")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @GetMapping
    public List<UserResponseDTO> getAll() {
        return userService.getAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @GetMapping("/id/{targetUserId}")
    public UserResponseDTO getById(@PathVariable Long targetUserId) {
        User user = userService.getById(targetUserId);
        return userMapper.toResponse(user);
    }

    @Operation(summary = "Get user by Username")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @GetMapping("/username/{targetUsername}")
    public UserResponseDTO getByUsername(@PathVariable String targetUsername) {
        User user = userService.getByUsername(targetUsername);
        return userMapper.toResponse(user);
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "409", description = "Username already exists")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserCreateDTO dto) {
        User user = userMapper.toCreate(dto);
        userService.create(user);
        return userMapper.toResponse(user);
    }

    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "404", description = "User not found")
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

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @DeleteMapping("/{targetUserId}")
    public void delete(@PathVariable Long targetUserId) {
        userService.delete(targetUserId);
    }
}
