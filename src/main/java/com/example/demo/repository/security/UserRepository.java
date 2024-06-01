package com.example.demo.repository.security;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findFirstByUsername(String username);
    Optional<UserEntity>findById(Long userId);


    // for news delete
    List<UserEntity> findAllByLikedComments(Comment comment);
    List<UserEntity> findAllByDislikedComments(Comment comment);
    List<UserEntity> findAllByComments(List<Comment> comments);
    // for seenNews
    List<UserEntity> findAllBySeenNews(News news);
    // for liked news
    List<UserEntity> findAllByLikedNews(News news);
    // for disliked news
    List<UserEntity> findAllByDislikedNews(News news);
    // for deleting user
    @Modifying // query changes the state of the database
    @Transactional // transactional query execution
    @Query(value = "DELETE FROM users_role WHERE user_id = :userId", nativeQuery = true)
    void deleteUserRole(@Param("userId") Long userId);

    //Find user by username
    @Query("Select c from users c WHERE c.username LIKE CONCAT('%', :username ,'%')")
    List<UserEntity> searchUser(@Param("username") String username);



}
