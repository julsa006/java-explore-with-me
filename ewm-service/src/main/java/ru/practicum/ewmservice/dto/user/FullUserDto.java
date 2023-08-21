package ru.practicum.ewmservice.dto.user;

import lombok.Value;

@Value
public class FullUserDto {
    Long id;
    String name;
    String email;
}
