package ru.practicum.ewmservice.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.comment.CommentMapper;
import ru.practicum.ewmservice.dto.comment.FullCommentDto;
import ru.practicum.ewmservice.service.CommentService;

@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
@Validated
public class AdminCommentController {
    private CommentService commentService;

    @PatchMapping("/{commentId}")
    public FullCommentDto updateEvent(@PathVariable Long commentId,
                                      @RequestParam(defaultValue = "true") boolean hidden) {
        return CommentMapper.toFullCommentDto(commentService.changeCommentVisibility(commentId, hidden));
    }

}
