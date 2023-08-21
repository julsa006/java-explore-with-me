package ru.practicum.ewmservice.dto.event;

import ru.practicum.ewmservice.dto.category.CategoryMapper;
import ru.practicum.ewmservice.dto.user.UserMapper;
import ru.practicum.ewmservice.model.Event;

public class EventMapper {
    public static EventDto toEventDto(Event event) {
        return new EventDto(event.getId(), event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getCreatedOn(), event.getDescription(), event.getEventDate(),
                UserMapper.toUserDto(event.getInitiator()), new LocationDto(event.getLocationLat(), event.getLocationLon()),
                event.isPaid(), event.getParticipantLimit(), event.getPublishedOn(), event.isRequestModeration(),
                event.getState(), event.getTitle(), event.getViews());
    }
}
