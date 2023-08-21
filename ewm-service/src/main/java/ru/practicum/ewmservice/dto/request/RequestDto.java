package ru.practicum.ewmservice.dto.request;

import lombok.Value;
import ru.practicum.ewmservice.model.RequestStatus;

import java.time.LocalDateTime;

@Value
public class RequestDto {
    Long id;
    Long event;
    LocalDateTime created;
    Long requester;
    RequestStatus status;
}
