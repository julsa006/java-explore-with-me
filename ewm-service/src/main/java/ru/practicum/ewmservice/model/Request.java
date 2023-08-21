package ru.practicum.ewmservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.EAGER)
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
