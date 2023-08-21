package ru.practicum.ewmservice.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exceptions.EntityNotFoundException;
import ru.practicum.ewmservice.model.Compilation;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.repository.CompilationRepository;
import ru.practicum.ewmservice.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;


    public Compilation createCompilation(String title, Boolean pinned, List<Long> eventIds) {
        List<Event> events = eventIds == null ? List.of() : eventRepository.findAllById(eventIds);
        return compilationRepository.save(new Compilation(null, title, pinned, events));
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    public Compilation updateCompilation(Long compId, String title, Boolean pinned, List<Long> events) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation not found"));
        if (events != null) {
            compilation.setEvents(eventRepository.findAllById(events));
        }
        if (title != null) {
            compilation.setTitle(title);
        }
        if (pinned != null) {
            compilation.setPinned(pinned);
        }
        return compilationRepository.save(compilation);
    }

    public Compilation getCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compilation not found"));
    }

    public List<Compilation> getCompilations(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return compilationRepository.findAll(page).getContent();
    }
}
