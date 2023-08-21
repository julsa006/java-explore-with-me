package ru.practicum.ewmservice.dto.request;

import ru.practicum.ewmservice.model.Request;
import ru.practicum.ewmservice.model.UpdateRequestStatusResponse;

import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getId(), request.getCreated(),
                request.getRequester().getId(), request.getStatus());
    }

    public static UpdateRequestStatusResponseDto toUpdateRequestStatusResponseDto(UpdateRequestStatusResponse response) {
        return new UpdateRequestStatusResponseDto(
                response.getConfirmed().stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()),
                response.getRejected().stream().map(RequestMapper::toRequestDto).collect(Collectors.toList())
        );
    }
}
