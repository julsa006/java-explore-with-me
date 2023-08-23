package ru.practicum.statsservice.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statsservice.dto.StatsDto;
import ru.practicum.statsservice.server.model.Hit;
import ru.practicum.statsservice.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsService {
    StatsRepository statsRepository;

    public void registerHit(String app, String uri, String ip, LocalDateTime timestamp) {
        statsRepository.save(new Hit(app, uri, ip, timestamp, null));
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris == null) {
                return statsRepository.countUniqueHitsStats(start, end);
            } else {
                return statsRepository.countUniqueHitsStatsWithUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statsRepository.countHitsStats(start, end);
            } else {
                return statsRepository.countHitsStatsWithUris(start, end, uris);
            }
        }
    }
}
