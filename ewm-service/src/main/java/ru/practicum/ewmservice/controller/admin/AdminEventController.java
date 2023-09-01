package ru.practicum.ewmservice.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.event.EventDto;
import ru.practicum.ewmservice.dto.event.EventMapper;
import ru.practicum.ewmservice.dto.event.LocationDto;
import ru.practicum.ewmservice.dto.event.UpdateAdminEventDto;
import ru.practicum.ewmservice.model.EventState;
import ru.practicum.ewmservice.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventDto createEvent(@PathVariable Long eventId,
                                @Valid @RequestBody UpdateAdminEventDto body) {
        return EventMapper.toEventDto(eventService.updateAdminEvent(eventId,
                body.getAnnotation(), body.getCategory(), body.getDescription(),
                body.getEventDate(), Optional.ofNullable(body.getLocation()).map(LocationDto::getLat).orElse(null),
                Optional.ofNullable(body.getLocation()).map(LocationDto::getLon).orElse(null),
                body.getPaid(), body.getParticipantLimit(), body.getRequestModeration(), body.getTitle(), body.getStateAction()));
    }

    @GetMapping
    public List<EventDto> getEvents(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<EventState> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(EventMapper::toEventDto).collect(Collectors.toList());
    }
}
