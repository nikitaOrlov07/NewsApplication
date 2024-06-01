package com.example.demo.repository;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByNewsId(Long newsId);
    List<Comment> findAllByAuthor(String user);
    Comment findCommentById (Long id);



}
