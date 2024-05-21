package com.example.demo.repository;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByNewsId(Long newsId);

    List<Comment> findAllByNewsId(Long id);

Comment findCommentById (Long id);



}
