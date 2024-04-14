package com.example.demo.services.impl;

import com.example.demo.models.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceimpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long clubId) {
        return commentRepository.findCommentByNewsId(clubId);
    }
}
