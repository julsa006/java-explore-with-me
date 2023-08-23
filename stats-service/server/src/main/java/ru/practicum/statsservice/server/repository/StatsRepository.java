package ru.practicum.statsservice.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsservice.dto.StatsDto;
import ru.practicum.statsservice.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "select new ru.practicum.statsservice.dto.StatsDto(h.app, h.uri, count(distinct h.ip))" +
            " from Hit as h " +
            " where (h.timestamp BETWEEN :start AND :end)" +
            " and h.uri in :uris" +
            " group by h.app, h.uri " +
            " order by count(distinct h.ip) desc")
    List<StatsDto> countUniqueHitsStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.statsservice.dto.StatsDto(h.app, h.uri, count(distinct h.ip))" +
            " from Hit as h " +
            " where (h.timestamp BETWEEN :start AND :end)" +
            " group by h.app, h.uri " +
            " order by count(distinct h.ip) desc")
    List<StatsDto> countUniqueHitsStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.statsservice.dto.StatsDto(h.app, h.uri, count(*))" +
            " from Hit as h " +
            " where (h.timestamp BETWEEN :start AND :end)" +
            " and h.uri in :uris" +
            " group by h.app, h.uri " +
            " order by count(*) desc")
    List<StatsDto> countHitsStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.statsservice.dto.StatsDto(h.app, h.uri, count(*))" +
            " from Hit as h " +
            " where (h.timestamp BETWEEN :start AND :end)" +
            " group by h.app, h.uri " +
            " order by count(*) desc")
    List<StatsDto> countHitsStats(LocalDateTime start, LocalDateTime end);


}
