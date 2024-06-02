package com.example.demo.api.service;

import com.example.demo.DTO.NewsDto;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.NewsService;
import com.example.demo.services.impl.CommentServiceimpl;
import com.example.demo.services.security.UserService;
import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsService newsService;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CommentServiceimpl commentService;
    private Comment comment; private News news; private NewsDto newsDto;private UserEntity user;


    @BeforeEach // executed before each test methods
    private void init()
    {
        news = News.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .commentsCount(0)
                .build();

        comment =Comment.builder()
                .id(1L)
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();

         user = UserEntity.builder()
                 .username("user")
                 .password("password")
                 .email("email")
                 .roleId(0)
                 .build();

    }

    @Test
    public void  CommentService_CreateComment_ReturnsComment()
    {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(newsService.getNewsById(news.getId())).thenReturn(news);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        when(userService.findByUsername(Mockito.any(String.class))).thenReturn(user);

        Comment savedComment =  commentService.saveComment(comment, news.getId());
        Assertions.assertNotNull(savedComment);
    }
    @Test
    public void CommentService_findByNewsId_ReturnsComment()
    {
        long newsId =1L;
        when(commentService.getComments(newsId)).thenReturn(Arrays.asList(comment));

        List<Comment> comments_List = commentService.getComments(newsId);

        Assertions.assertNotNull(comments_List);
    }
    @Test
    public void CommentService_findByCommentId_ReturnsComment()
    {

        when(commentService.findCommentById(comment.getId())).thenReturn(comment);

        Comment returnedComment = commentService.findCommentById(comment.getId());

        Assertions.assertNotNull(returnedComment);
        Assertions.assertEquals(Comment.class, returnedComment.getClass(),"This must be the same classes");
        Assertions.assertEquals(comment,returnedComment,"this two objects must be equal");
    }
    @Test
    public void CommentService_DeleteComment_ReturnVoid()
    {
       comment.setNews(news);
       news.setComments(Collections.singletonList(comment));
       when(newsService.getNewsById(news.getId())).thenReturn(news);

       Assertions.assertAll(() -> commentService.deleteComment(comment,comment.getNews().getId()));
    }




}
