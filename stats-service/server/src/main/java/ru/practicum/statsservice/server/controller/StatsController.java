package ru.practicum.statsservice.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsservice.dto.HitDto;
import ru.practicum.statsservice.dto.StatsDto;
import ru.practicum.statsservice.server.exceptions.ValidationError;
import ru.practicum.statsservice.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity registerHit(@RequestBody HitDto hit) {
        statsService.registerHit(hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationError("Start date is after end date");
        }
        return statsService.getStats(start, end, uris, unique);
    }

}
