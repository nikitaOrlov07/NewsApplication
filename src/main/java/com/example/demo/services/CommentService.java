package com.example.demo.services;

import com.example.demo.models.Comment;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment);

    List<Comment> getComments(Long clubId);
}
