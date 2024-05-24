package com.example.demo.repository.security;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findFirstByUsername(String username);
    Optional<UserEntity> findById(Long userId);


    // for comments
    List<UserEntity> findAllByLikedComments(Comment comment);
    List<UserEntity> findAllByDislikedComments(Comment comment);
    List<UserEntity> findAllByComments(List<Comment> comments);
    // for seenNews
    List<UserEntity> findAllBySeenNews(News news);
    // for liked news
    List<UserEntity> findAllByLikedNews(News news);
    // for disliked news
    List<UserEntity> findAllByDislikedNews(News news);
}
