package ru.practicum.ewmservice.dto.compilation;

import lombok.Value;
import ru.practicum.ewmservice.dto.event.EventDto;

import java.util.List;

@Value
public class CompilationDto {
    Long id;
    String title;
    boolean pinned;
    List<EventDto> events;
}
