package ru.practicum.ewmservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exceptions.EntityNotFoundException;
import ru.practicum.ewmservice.exceptions.RequestConditionException;
import ru.practicum.ewmservice.model.*;
import ru.practicum.ewmservice.repository.EventRepository;
import ru.practicum.ewmservice.repository.RequestRepository;
import ru.practicum.ewmservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public Request createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestConditionException("Event initiator cant create request");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new RequestConditionException("Event state isnt published");
        }
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestConditionException("Request already exist");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0 && limit <= event.getConfirmedRequests()) {
            throw new RequestConditionException("Requests limit");
        }
        Request request = new Request(null, event, LocalDateTime.now(), user,
                limit == 0 || !event.isRequestModeration() ? RequestStatus.CONFIRMED : RequestStatus.PENDING);
        return requestRepository.save(request);
    }


    public Request cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new EntityNotFoundException("Request not found");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    public List<Request> getRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return requestRepository.findAllByRequesterId(userId);
    }

    public List<Request> getUserRequests(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException("Request not found");
        }
        return requestRepository.findAllByEventId(eventId);
    }

    public UpdateRequestStatusResponse updateRequestsStatus(Long userId, Long eventId,
                                                            List<Long> requestIds, RequestStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException("Request not found");
        }
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            return new UpdateRequestStatusResponse(List.of(), List.of());
        }
        int confirmedRequests = event.getConfirmedRequests();
        int limit = event.getParticipantLimit();
        List<Request> requests = requestRepository.findAllById(requestIds);
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        for (Request request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new RequestConditionException("Request is not pending");
            }
            if (status == RequestStatus.CONFIRMED) {
                if (confirmedRequests >= limit) {
                    throw new RequestConditionException("Limit");
                }
                confirmedRequests++;
                request.setStatus(RequestStatus.CONFIRMED);
                confirmed.add(request);
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejected.add(request);
            }
        }
        requestRepository.saveAll(requests);
        return new UpdateRequestStatusResponse(confirmed, rejected);
    }
}
