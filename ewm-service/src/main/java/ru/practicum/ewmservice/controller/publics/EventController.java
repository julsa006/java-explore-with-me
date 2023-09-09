package ru.practicum.ewmservice.controller.publics;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.comment.CommentDto;
import ru.practicum.ewmservice.dto.comment.CommentMapper;
import ru.practicum.ewmservice.dto.event.EventDto;
import ru.practicum.ewmservice.dto.event.EventMapper;
import ru.practicum.ewmservice.model.GetEventsRequest;
import ru.practicum.ewmservice.service.CommentService;
import ru.practicum.ewmservice.service.EventService;
import ru.practicum.statsservice.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final StatsClient statsClient;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        statsClient.hit(request.getRequestURI(), request.getRemoteAddr());
        return EventMapper.toEventDto(eventService.getPublishedEvent(id));
    }

    @GetMapping
    public List<EventDto> getEvents(@RequestParam(required = false) String text,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) Boolean paid,
                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                    @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size,
                                    HttpServletRequest request) {
        statsClient.hit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getEvents(GetEventsRequest.builder()
                        .text(text)
                        .categories(categories)
                        .paid(paid)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .onlyAvailable(onlyAvailable)
                        .sort(sort)
                        .from(from)
                        .size(size).build())
                .stream().map(EventMapper::toEventDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}/comments")
    public List<CommentDto> getEventComments(@PathVariable Long id,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return commentService.getVisibleComments(id, from, size).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

}
