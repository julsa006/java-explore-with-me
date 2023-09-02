package ru.practicum.ewmservice.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exceptions.EntityNotFoundException;
import ru.practicum.ewmservice.model.Comment;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.User;
import ru.practicum.ewmservice.repository.CommentRepository;
import ru.practicum.ewmservice.repository.EventRepository;
import ru.practicum.ewmservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Long userId, Long eventId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return commentRepository.save(new Comment(null, event, user, text, LocalDateTime.now(), false, null));
    }

    @Transactional
    public Comment updateComment(Long userId, Long commentId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(userId) || comment.isHidden()) {
            throw new EntityNotFoundException("Comment not found");
        }
        comment.setText(text);
        comment.setEdited(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment changeCommentVisibility(Long id, boolean hidden) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setHidden(hidden);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(userId) || comment.isHidden()) {
            throw new EntityNotFoundException("Comment not found");
        }
        commentRepository.delete(comment);
    }

    public List<Comment> getVisibleComments(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        PageRequest page = PageRequest.of(from / size, size, Sort.by("created").ascending());
        return commentRepository.findAllByEventIdAndHidden(eventId, false, page);
    }

    public List<Comment> getComments(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        PageRequest page = PageRequest.of(from / size, size, Sort.by("created").ascending());
        return commentRepository.findAllByEventId(eventId, page);
    }

}
