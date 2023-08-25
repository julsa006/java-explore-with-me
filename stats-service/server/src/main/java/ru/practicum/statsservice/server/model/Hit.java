package ru.practicum.statsservice.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
