package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;
import org.example.entity.Ad;
import org.example.entity.Comment;
import org.example.entity.User;
import org.example.enums.AccountType;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.repository.AdRepository;
import org.example.repository.UserRepository;
import org.example.service.CommentService;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final UserService userService;

    // Добавляю комментарий
    @Override
    public CommentResponse addComment(CommentRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден."));

        Ad ad = adRepository.findById(request.getAdId())
                .orElseThrow(() -> new IllegalArgumentException("Объявление с ID " + request.getAdId() + " не найдено."));

        if (user.getAccountType() == AccountType.BASIC) {
            long adCount = adRepository.countByOwnerId(user.getId());
            if (adCount >= 1) {
                throw new IllegalArgumentException("Пользователи с BASIC аккаунтом могут создавать только одно объявление.");
            }
        }

        Comment comment = CommentMapper.mapToEntity(request, ad, user);
        return CommentMapper.mapToResponse(commentRepository.save(comment));
    }

    // Получаю все комментарии для объявления
    @Override
    public List<CommentResponse> getCommentsForAd(Long adId) {

        List<Comment> comments = commentRepository.findByAdId(adId);

        return comments.stream()
                .map(CommentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю комментарии пользователя для объявления
    @Override
    public List<CommentResponse> getCommentsByUserForAd(Long adId, Long userId) {
        List<Comment> comments = commentRepository.findByUserIdAndAdId(userId, adId);
        return comments.stream()
                .map(CommentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    // Получаю конкретный комментарий по ID и ID объявления
    @Override
    public CommentResponse getCommentForAdById(Long adId, Long commentId) {
        Comment comment = commentRepository.findByAdIdAndId(adId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с таким ID не найден"));
        return CommentMapper.mapToResponse(comment);
    }

    // Удаляю комментарий
    @Override
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с ID " + commentId + " не найден."));

        User owner = comment.getUser();
        if (!userService.isCurrentUserOrAdmin(owner.getEmail(), owner.getId())) {
            throw new SecurityException("Вы не можете удалить этот комментарий.");
        }

        commentRepository.delete(comment);
    }
}