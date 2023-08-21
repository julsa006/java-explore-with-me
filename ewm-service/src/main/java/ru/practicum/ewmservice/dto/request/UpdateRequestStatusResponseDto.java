package ru.practicum.ewmservice.dto.request;

import lombok.Value;

import java.util.List;

@Value
public class UpdateRequestStatusResponseDto {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
