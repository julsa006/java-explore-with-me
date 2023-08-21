package ru.practicum.ewmservice.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.dto.NullOrNotBlank;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAdminEventDto {
    @NullOrNotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @NullOrNotBlank
    @Size(min = 20, max = 7000)
    String description;
    LocalDateTime eventDate;
    @Valid
    LocationDto location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    @NullOrNotBlank
    @Size(min = 3, max = 120)
    String title;
    AdminEventStateAction stateAction;
}
