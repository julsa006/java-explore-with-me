package ru.practicum.ewmservice.controller.privates;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.event.*;
import ru.practicum.ewmservice.dto.request.RequestDto;
import ru.practicum.ewmservice.dto.request.RequestMapper;
import ru.practicum.ewmservice.dto.request.UpdateRequestStatusResponseDto;
import ru.practicum.ewmservice.dto.request.UpdateRequestsStatusDto;
import ru.practicum.ewmservice.service.EventService;
import ru.practicum.ewmservice.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@PathVariable Long userId, @Valid @RequestBody CreateEventDto body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.toEventDto(eventService.createEvent(userId, body.getAnnotation(),
                body.getCategory(), body.getDescription(),
                body.getEventDate(), body.getLocation().getLat(), body.getLocation().getLon(),
                body.isPaid(), body.getParticipantLimit(), body.isRequestModeration(), body.getTitle())));
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return EventMapper.toEventDto(eventService.getUserEvent(userId, eventId));
    }

    @GetMapping()
    public List<EventDto> getEvents(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getUserEvents(userId, from, size).stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }


    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                @Valid @RequestBody UpdateUserEventDto body) {
        return EventMapper.toEventDto(eventService.updateUserEvent(userId, eventId,
                body.getAnnotation(), body.getCategory(), body.getDescription(),
                body.getEventDate(), Optional.ofNullable(body.getLocation()).map(LocationDto::getLat).orElse(null),
                Optional.ofNullable(body.getLocation()).map(LocationDto::getLon).orElse(null),
                body.getPaid(), body.getParticipantLimit(), body.getRequestModeration(), body.getTitle(), body.getStateAction()));
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getUserRequests(userId, eventId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public UpdateRequestStatusResponseDto updateRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                         @Valid @RequestBody UpdateRequestsStatusDto body) {
        return RequestMapper.toUpdateRequestStatusResponseDto(
                requestService.updateRequestsStatus(userId, eventId, body.getRequestIds(), body.getStatus()));
    }


}
