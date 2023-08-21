package ru.practicum.ewmservice.dto.event;

import lombok.Value;
import ru.practicum.ewmservice.dto.category.CategoryDto;
import ru.practicum.ewmservice.dto.user.UserDto;
import ru.practicum.ewmservice.model.EventState;

import java.time.LocalDateTime;

@Value
public class EventDto {
    Long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserDto initiator;
    LocationDto location;
    boolean paid;
    int participantLimit;
    LocalDateTime publishedOn;
    boolean requestModeration;
    EventState state;
    String title;
    Long views;
}
