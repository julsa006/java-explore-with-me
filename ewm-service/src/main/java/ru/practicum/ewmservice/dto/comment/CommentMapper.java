package ru.practicum.ewmservice.dto.comment;

import ru.practicum.ewmservice.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getEvent().getId(),
                comment.getUser().getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getEdited() != null);
    }

    public static FullCommentDto toFullCommentDto(Comment comment) {
        return new FullCommentDto(comment.getId(),
                comment.getEvent().getId(),
                comment.getUser().getId(),
                comment.getText(),
                comment.getCreated(),
                comment.isHidden(),
                comment.getEdited());
    }
}
