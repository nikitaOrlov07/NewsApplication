package com.example.demo.api.service;

import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.security.UserRepository;
import com.example.demo.services.CommentService;
import com.example.demo.services.impl.NewsServiceimpl;
import com.example.demo.services.security.UserService;
import com.example.demo.services.security.UserServiceimpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.services.security.UserServiceimpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {
    @Mock // instead of using Mockito.mock()
    private NewsRepository newsRepository;  // mock repository - and we won`t use application database
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentService;
    @InjectMocks
    private NewsServiceimpl newsService; //  @InjectMocks creates an instance of the NewsServiceimpl class and automatically injects into it all mock objects that were declared in the same test class and are of a matching type

    @Test
    public void NewsService_CreateNews_ReturnsNewsDto()
    {
        // Arrange
        News news = News.builder()
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();
        NewsDto newsDto = NewsDto.builder()
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();
        when(newsRepository.save(Mockito.any(News.class))).thenReturn(news); // When the save method of the newsRepository mock object is called with any object of the News class as an argument, return the news object I just created

        // Act
        News savedNews= newsService.createNews(newsDto); // in service method i use mapper and save News object into database
        // Assert
        Assertions.assertNotNull(savedNews);
    }
    @Test
    public void NewsService_GetAll_ReturnNews()
    {
        // Arrange
        Page<News> news = mock(Page.class); // create mock  object (will have the same methods as a real Page object)

        when(newsRepository.findAll(Mockito.any(Pageable.class))).thenReturn(news);

        // Act
        NewsPagination savedNews = newsService.getAllNews(1,10);

        // Assertion
        Assertions.assertNotNull(savedNews);
    }
    @Test
    public void NewsService_GetNewsById_ReturnNews() {
        // Arrange
        News news = News.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();

        when(newsRepository.findById(1L)).thenReturn(Optional.ofNullable(news));

        // Act
        News returnedNews = newsService.getNewsById(1L);

        // Assert
        Assertions.assertNotNull(returnedNews);
        // Additional assertions on the returned news object
    }
    // Update
    @Test
    public void NewsService_UpdateNews_ReturnNews() {
        // Arrange
        News news = News.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();

        when(newsRepository.findById(1L)).thenReturn(Optional.ofNullable(news));
        when(newsRepository.save(Mockito.any(News.class))).thenReturn(news);
        News returnedNews = newsRepository.findById(1L).get();
        returnedNews.setDescription("Changed description");

        News savedNews = newsService.updateNews(returnedNews);

        // Assert
        Assertions.assertNotNull(savedNews.getDescription());
        Assertions.assertEquals("Changed description", savedNews.getDescription());

        // Additional assertions on the returned news object
    }

    // Delete
    @Test
    public void NewsService_DeleteNews_DeleteNews() {
        // Arrange
        News news = News.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();

        when(newsRepository.findById(1L)).thenReturn(Optional.ofNullable(news));
        News returnedNews = newsRepository.findById(1L).get();
        returnedNews.setDescription("Changed description");

        // Assert
        Assertions.assertAll(() -> newsService.deleteNews(news));
    }



}
