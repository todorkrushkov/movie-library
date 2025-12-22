package telerik.project.movielibrary.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import telerik.project.movielibrary.helpers.mappers.UserMapper;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;
import telerik.project.movielibrary.models.dtos.api.ApiResponseDTO;
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

    @Operation(summary = "Get all users with filters")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @GetMapping
    public ApiResponseDTO<List<UserResponseDTO>> getAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Role role
            ) {
        List<UserResponseDTO> users = userService
                .getAll(username, role)
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "Users retrieved successfully.",
                users
        );
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @GetMapping("/{targetUserId}")
    public ApiResponseDTO<UserResponseDTO> getById(@PathVariable Long targetUserId) {
        User user = userService.getById(targetUserId);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                null,
                userMapper.toResponse(user)
        );
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "409", description = "Username already exists")
    @PreAuthorize("@validateAuth.isAdmin(authentication)")
    @PostMapping
    public ApiResponseDTO<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        User user = userMapper.toCreate(dto);
        userService.create(user);

        return ApiResponseDTO.success(
                HttpStatus.CREATED.value(),
                "User created successfully.",
                userMapper.toResponse(user)
        );
    }

    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @PutMapping("/{targetUserId}")
    public ApiResponseDTO<UserResponseDTO> update(
            @PathVariable Long targetUserId,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        User targetUser = userService.getById(targetUserId);
        userMapper.toUpdate(targetUser, dto);
        userService.update(targetUserId, targetUser);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "User updated successfully.",
                userMapper.toResponse(targetUser)
        );
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("@validateAuth.isOwnerOrAdmin(#targetUserId, authentication)")
    @DeleteMapping("/{targetUserId}")
    public ApiResponseDTO<Void> delete(@PathVariable Long targetUserId) {
        userService.delete(targetUserId);

        return ApiResponseDTO.success(
                HttpStatus.OK.value(),
                "User deleted successfully.",
                null
        );
    }
}
