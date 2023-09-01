package ru.practicum.ewmservice.model;

import lombok.Value;

import java.util.List;

@Value
public class UpdateRequestStatusResponse {
    List<Request> confirmed;
    List<Request> rejected;
}
