package com.example.demo.services.impl;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.security.UserRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.example.demo.services.NewsService;
@Service
public class CommentServiceimpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;


    public Comment saveComment(Comment comment,Long newsId) {

        UserEntity user =userService.findByUsername(SecurityUtil.getSessionUser());
        News news = newsService.getNewsById(newsId);
        comment.setNews(news);
        news.setCommentsCount(news.getCommentsCount() + 1);

        comment.setAuthor(user.getUsername());
        newsService.updateNews(news);
        return  commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long newsId) {
        return commentRepository.findCommentByNewsId(newsId);
    }

    @Override
    public void deleteComment (Comment comment, Long newsId) {


        News news = newsService.getNewsById(newsId);
        news.setCommentsCount(news.getCommentsCount() - 1);

        List<UserEntity> usersWhoLiked = userService.findAllByLikedComments(comment);
        List<UserEntity> usersWhoDisLiked= userService.findAllByDislikedComments(comment);
        if(usersWhoLiked != null || usersWhoDisLiked != null) {
            for (UserEntity user : usersWhoLiked) {
                user.getLikedComments().remove(comment);
                userService.updateUser(user);
            }
            for (UserEntity user : usersWhoDisLiked) {
                user.getDislikedComments().remove(comment);
                userService.updateUser(user);
            }
        }
        newsService.updateNews(news);
        commentRepository.delete(comment);
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentRepository.findCommentById(commentId);
    }
}
