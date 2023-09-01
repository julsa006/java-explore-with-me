package ru.practicum.ewmservice.dto.user;

import ru.practicum.ewmservice.model.User;

public class UserMapper {
    public static FullUserDto toFullUserDto(User user) {
        return new FullUserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }
}
