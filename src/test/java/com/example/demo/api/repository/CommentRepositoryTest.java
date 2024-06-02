package com.example.demo.api.repository;

import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // configure H2 test database (not use our application database)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void CommentRepository_saveAll_ReturnSavedComment() // test newer return anything (use AAA pattern)
    {
        Comment comment=Comment.builder()
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();

        // Act
        Comment savedComment = commentRepository.save(comment);

        // Assert
        Assertions.assertNotNull(savedComment);
        Assertions.assertTrue(savedComment.getId()>0);
    }
    @Test
    public void CommentRepository_GetAll_ReturnsMoreThanOneComment()
    {
        // Arrange
        Comment comment1 =Comment.builder()
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();
        Comment comment2 = Comment.builder()
                .text("comment2")
                .author("author2")
                .pubDate("2022-02-07")
                .build();
        commentRepository.save(comment1); commentRepository.save(comment2);
        // Act
        List<Comment> commentList= commentRepository.findAll();
        // Assert
        Assertions.assertNotNull(commentList);
        Assertions.assertEquals(2,commentList.size(),"NewsList size must be 2");
    }
    @Test
    public void NewsRepository_GetById_ReturnEqObject()
    {
        // Arrange
        Comment comment =Comment.builder()
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();
        commentRepository.save(comment);
        // Act
        Comment returnedComment = commentRepository.findCommentById(comment.getId());
        // Assert
        Assertions.assertNotNull(returnedComment);
        Assertions.assertEquals(comment,returnedComment,"this two objects must be equals");
    }
    // Update
    @Test
    public void CommentRepository_Update_ReturnComment()
    {
        // Arrange
        Comment comment =Comment.builder()
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();



        commentRepository.save(comment);

        // Act
        Comment returnedComment = commentRepository.findCommentById(comment.getId());


        returnedComment.setText("Changed text");
        returnedComment.setAuthor("Changed author");

       Comment updatedComment = commentRepository.save(returnedComment);
        //Assertions
        Assertions.assertNotNull(updatedComment.getText());
        Assertions.assertNotNull(updatedComment.getAuthor());

        Assertions.assertEquals("Changed text",updatedComment.getText());
        Assertions.assertEquals("Changed author",updatedComment.getAuthor());
    }
    // Delete
    @Test
    public void CommentRepository_Delete_ReturnNull()
    {
        // Arrange
        Comment comment =Comment.builder()
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .build();
        commentRepository.save(comment);

        //Act
        commentRepository.deleteById(comment.getId());
        Optional <Comment> returnedComment = commentRepository.findById(comment.getId());
        Assertions. assertFalse(returnedComment.isPresent(), "Returned news should be empty after deletion"); // assertFalse - used to check if the condition is false , isPresent method - returns true if there is a value in the object optional
    }
}
