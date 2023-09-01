package ru.practicum.ewmservice.controller.privates;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.request.RequestDto;
import ru.practicum.ewmservice.dto.request.RequestMapper;
import ru.practicum.ewmservice.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RequestMapper.toRequestDto(requestService.createRequest(userId, eventId)));
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId).stream().map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return RequestMapper.toRequestDto(requestService.cancelRequest(userId, requestId));
    }


}
