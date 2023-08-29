package ru.practicum.ewmservice.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dto.event.AdminEventStateAction;
import ru.practicum.ewmservice.dto.event.UserEventStateAction;
import ru.practicum.ewmservice.exceptions.EntityNotFoundException;
import ru.practicum.ewmservice.exceptions.IllegalEntityStateException;
import ru.practicum.ewmservice.exceptions.ValidationException;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;
import ru.practicum.ewmservice.repository.CategoryRepository;
import ru.practicum.ewmservice.repository.EventRepository;
import ru.practicum.ewmservice.repository.UserRepository;
import ru.practicum.statsservice.client.StatsClient;
import ru.practicum.statsservice.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatsClient statsClient;

    @Transactional
    public Event createEvent(Long userId, String annotation, Long categoryId, String description,
                             LocalDateTime eventDate, double lat, double lon, boolean paid, int participantLimit,
                             boolean requestModeration, String title) {
        Event event = new Event();
        event.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")));
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date must be 2 hours from now");
        }
        event.setCreatedOn(LocalDateTime.now());
        event.setAnnotation(annotation);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setLocationLat(lat);
        event.setLocationLon(lon);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setRequestModeration(requestModeration);
        event.setTitle(title);
        event.setState(EventState.PENDING);
        return setViews(eventRepository.save(event));
    }


    public Event getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException("Event not found");
        }
        return setViews(event);
    }


    @Transactional
    public Event updateUserEvent(Long userId, Long eventId, String annotation, Long categoryId, String description,
                                 LocalDateTime eventDate, Double lat, Double lon, Boolean paid, Integer participantLimit,
                                 Boolean requestModeration, String title, UserEventStateAction stateAction) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException("Event not found");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new IllegalEntityStateException("Only pending or canceled events can be changed");
        }

        updateEvent(event, annotation, categoryId, description, eventDate, lat, lon, paid,
                participantLimit, requestModeration, title);
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        return setViews(eventRepository.save(event));
    }

    @Transactional
    public Event updateAdminEvent(Long eventId, String annotation, Long categoryId, String description,
                                  LocalDateTime eventDate, Double lat, Double lon, Boolean paid,
                                  Integer participantLimit, Boolean requestModeration, String title,
                                  AdminEventStateAction stateAction) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new IllegalEntityStateException("Only pending or canceled events can be changed");
        }

        updateEvent(event, annotation, categoryId, description, eventDate, lat, lon, paid,
                participantLimit, requestModeration, title);

        if (stateAction != null) {
            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        throw new IllegalEntityStateException("Only pending events can be published");
                    }
                    event.setPublishedOn(LocalDateTime.now());
                    if (event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                        throw new IllegalEntityStateException("Event date must be more than hour before published date");
                    }
                    event.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    if (event.getState() == EventState.PUBLISHED) {
                        throw new IllegalEntityStateException("Only not published events can be canceled");
                    }
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        return setViews(eventRepository.save(event));

    }

    private void updateEvent(Event event, String annotation, Long categoryId, String description,
                             LocalDateTime eventDate, Double lat, Double lon, Boolean paid,
                             Integer participantLimit, Boolean requestModeration, String title) {
        if (categoryId != null) {
            event.setCategory(categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found")));
        }
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Event date must be 2 hours from now");
            }
            event.setEventDate(eventDate);
        }
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (description != null) {
            event.setDescription(description);
        }

        if (lat != null) {
            event.setLocationLat(lat);
        }
        if (lon != null) {
            event.setLocationLon(lon);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
    }

    public List<Event> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return setViews(eventRepository.getAdminEvents(users, states, categories, rangeStart, rangeEnd, page));
    }

    public Event getPublishedEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (event.getState() != EventState.PUBLISHED) {
            throw new EntityNotFoundException("Event not found");
        }
        return setViews(event);
    }

    public List<Event> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        PageRequest page;
        switch (sort) {
            case "EVENT_DATE":
                page = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
                break;
            case "VIEWS":
                page = null;
                break;
            default:
                throw new ValidationException(String.format("Sort by %s does not exist", sort));
        }
        return setViews(eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, page));
    }

    private Event setViews(Event event) {
        setViews(List.of(event));
        return event;
    }

    private List<Event> setViews(List<Event> events) {
        List<String> allUris = events.stream()
                .map((e) -> String.format("/events/%d", e.getId()))
                .collect(Collectors.toList());

        List<StatsDto> stats = statsClient.getStats(LocalDateTime.of(2000, 1, 1, 0, 0, 0), LocalDateTime.of(2100, 1, 1, 0, 0, 0), allUris, true);

        Map<Long, Long> viewsMap = stats.stream()
                .filter((e) -> e.getUri().startsWith("/events/"))
                .collect(Collectors.groupingBy(
                        (StatsDto s) -> Long.parseLong(s.getUri().substring("/events/".length())),
                        Collectors.summingLong(StatsDto::getHits)
                ));

        events.forEach((e) -> e.setViews(viewsMap.getOrDefault(e.getId(), 0L)));
        return events;
    }

    public List<Event> getUserEvents(Long userId, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
        return setViews(eventRepository.findAllByInitiatorId(userId, page));
    }

}
