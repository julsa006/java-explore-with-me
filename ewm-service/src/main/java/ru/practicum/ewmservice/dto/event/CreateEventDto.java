package ru.practicum.ewmservice.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateEventDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    @Valid
    LocationDto location;
    boolean paid = false;
    int participantLimit = 0;
    boolean requestModeration = true;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
