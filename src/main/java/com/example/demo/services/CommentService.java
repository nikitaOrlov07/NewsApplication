package com.example.demo.services;

import com.example.demo.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    void saveComment(Comment comment,Long newsId);

    List<Comment> getComments(Long newsId);

    void deleteComment (Comment comment, Long newsId);
    Comment findCommentById(Long commentId);
}
