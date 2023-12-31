package ru.practicum.statsservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsservice.dto.HitDto;
import ru.practicum.statsservice.dto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${stats-service.url}")
    private String baseUrl;
    @Value("${app-name}")
    private String appName;

    public void hit(String uri, String ip) {
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/hit",
                new HitDto(appName, uri, ip, LocalDateTime.now()), Void.class);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new RequestFailedException(response.getBody().toString(), response.getStatusCode());
        }
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMATTER));
        parameters.put("end", end.format(DATE_TIME_FORMATTER));
        parameters.put("unique", unique);
        parameters.put("uris", uris);
        StringBuilder requestParams = new StringBuilder("/stats?start={start}&end={end}&unique={unique}");
        if (uris != null) {
            for (String uri : uris) {
                requestParams.append(String.format("&uris=%s", uri));
            }
        }
        ResponseEntity<StatsDto[]> response = restTemplate.getForEntity(baseUrl + requestParams, StatsDto[].class, parameters);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RequestFailedException(response.getBody().toString(), response.getStatusCode());
        }
        return Arrays.asList(response.getBody());
    }
}
