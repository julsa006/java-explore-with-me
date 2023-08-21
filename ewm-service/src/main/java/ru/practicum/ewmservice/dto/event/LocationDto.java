package ru.practicum.ewmservice.dto.event;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class LocationDto {
    @NotNull
    double lat;
    @NotNull
    double lon;
}
