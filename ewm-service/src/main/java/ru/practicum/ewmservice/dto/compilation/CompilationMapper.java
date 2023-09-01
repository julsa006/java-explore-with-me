package ru.practicum.ewmservice.dto.compilation;

import ru.practicum.ewmservice.dto.event.EventMapper;
import ru.practicum.ewmservice.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.isPinned(),
                compilation.getEvents().stream().map(EventMapper::toEventDto).collect(Collectors.toList()));
    }
}
