package ru.practicum.ewmservice.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.user.CreateUserDto;
import ru.practicum.ewmservice.dto.user.FullUserDto;
import ru.practicum.ewmservice.dto.user.UserMapper;
import ru.practicum.ewmservice.model.User;
import ru.practicum.ewmservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<FullUserDto> createUser(@Valid @RequestBody CreateUserDto body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toFullUserDto(userService.createUser(body.getName(), body.getEmail())));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size) {
        if (ids == null) {
            return userService.getUsers(from, size);
        }
        return userService.getUsersByIds(ids);
    }
}
