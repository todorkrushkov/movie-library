package telerik.project.movielibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import telerik.project.movielibrary.exceptions.EntityDuplicateException;
import telerik.project.movielibrary.exceptions.EntityNotFoundException;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> getAll() {
        return userService.getAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @PreAuthorize("@securityUtil.canModify(#targetUserId, authentication)")
    @GetMapping("/{targetUserId}")
    public UserResponseDTO getById(@PathVariable Long targetUserId) {
        try {
            User user = userService.getById(targetUserId);
            return userMapper.toResponse(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserCreateDTO dto) {
        try {
            User user = userMapper.toCreate(dto);
            userService.create(user);
            return userMapper.toResponse(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PreAuthorize("@securityUtil.canModify(#targetUserId, authentication)")
    @PutMapping("/{targetUserId}")
    public UserResponseDTO update(
            @PathVariable Long targetUserId,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        try {
            User targetUser = userService.getById(targetUserId);
            userMapper.toUpdate(targetUser, dto);
            userService.update(targetUserId, targetUser);
            return userMapper.toResponse(targetUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PreAuthorize("@securityUtil.canModify(#targetUserId, authentication)")
    @DeleteMapping("/{targetUserId}")
    public void delete(@PathVariable Long targetUserId) {
        try {
            userService.delete(targetUserId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
