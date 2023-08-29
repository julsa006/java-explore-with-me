package ru.practicum.ewmservice.controller.publics;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.event.EventDto;
import ru.practicum.ewmservice.dto.event.EventMapper;
import ru.practicum.ewmservice.exceptions.ValidationException;
import ru.practicum.ewmservice.service.EventService;
import ru.practicum.statsservice.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    private final StatsClient statsClient;

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
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        statsClient.hit(request.getRequestURI(), request.getRemoteAddr());
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start date is after end date");
        }
        if (sort == null) {
            sort = "EVENT_DATE";
        }
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size).stream()
                .map(EventMapper::toEventDto).collect(Collectors.toList());
    }


}