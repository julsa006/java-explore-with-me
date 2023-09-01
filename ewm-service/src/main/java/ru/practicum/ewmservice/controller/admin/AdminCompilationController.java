package ru.practicum.ewmservice.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.compilation.CompilationDto;
import ru.practicum.ewmservice.dto.compilation.CompilationMapper;
import ru.practicum.ewmservice.dto.compilation.CreateCompilationDto;
import ru.practicum.ewmservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.ewmservice.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminCompilationController {
    private CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody CreateCompilationDto body) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CompilationMapper.toCompilationDto(compilationService
                        .createCompilation(body.getTitle(), body.isPinned(), body.getEvents())));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @Valid @RequestBody UpdateCompilationDto body) {
        return CompilationMapper.toCompilationDto(
                compilationService.updateCompilation(compId,
                        body.getTitle(), body.getPinned(), body.getEvents()));
    }
}
