package ru.practicum.ewmservice.dto.user;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserDto {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    String name;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    String email;
}
