package ru.practicum.ewmservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private String text;
    private LocalDateTime created;
    private boolean edited;
}
