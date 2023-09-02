package ru.practicum.ewmservice.controller.privates;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.comment.CommentDto;
import ru.practicum.ewmservice.dto.comment.CommentMapper;
import ru.practicum.ewmservice.dto.comment.CreateUpdateCommentDto;
import ru.practicum.ewmservice.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
@Validated
public class PrivateCommentController {
    private CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto updateEvent(@PathVariable Long userId, @PathVariable Long commentId,
                                  @Valid @RequestBody CreateUpdateCommentDto comment) {
        return CommentMapper.toCommentDto(commentService.updateComment(userId, commentId, comment.getText()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
