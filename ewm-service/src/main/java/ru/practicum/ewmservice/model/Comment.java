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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private String text;
    private LocalDateTime created;
    private boolean hidden;
    private LocalDateTime edited;
}
