package org.example.mapper;

import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;
import org.example.entity.Ad;
import org.example.entity.Comment;
import org.example.entity.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment mapToEntity(CommentRequest request, Ad ad, User user) {
        return Comment.builder()
                .text(request.getText())
                .ad(ad)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getUser().getEmail(),
                comment.getCreatedAt()
        );
    }
}