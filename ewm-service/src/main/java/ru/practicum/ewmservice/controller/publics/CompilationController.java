package ru.practicum.ewmservice.controller.publics;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.model.Compilation;
import ru.practicum.ewmservice.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public Compilation getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<Compilation> getCompilations(@RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(from, size);
    }
}
