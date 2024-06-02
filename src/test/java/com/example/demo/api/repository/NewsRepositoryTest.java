package com.example.demo.api.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import com.example.demo.models.News;
import com.example.demo.repository.NewsRepository;
import org.hamcrest.MatcherAssert;
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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // configure H2 test database
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void NewsRepository_saveAll_ReturnSavedNews() // test newer return anything (use AAA pattern)
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
        // Act
        News savedNews = newsRepository.save(news);

        // Assert
        Assertions.assertNotNull(savedNews);
        Assertions.assertTrue(savedNews.getId()>0);
    }
    @Test
    public void NewsRepository_GetAll_ReturnsMoreThanOneNews()
    {
        // Arrange
        News news1 = News.builder()
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();
        News news2 = News.builder()
                .title("News2")
                .language("slovakian")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();
        newsRepository.save(news1);newsRepository.save(news2);
        // Act
        List<News> newsList= newsRepository.findAll();
        // Assert
        Assertions.assertNotNull(newsList);
        Assertions.assertEquals(2,newsList.size(),"NewsList size must be 2");
    }
    @Test
    public void NewsRepository_GetById_ReturnEqObject()
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
        newsRepository.save(news);
        // Act
        News returningNews = newsRepository.findById(news.getId()).get(); // this method return Optional --> because of this i use .get()
        // Assert
        Assertions.assertNotNull(returningNews);
        Assertions.assertEquals(news,returningNews,"this two objects must be equals");
    }
    // Testing Custom Query Method
    @Test
    public void NewsRepository_GetByTittle_ReturnEqObject()
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
        newsRepository.save(news);
        // Act
        News returnNews = newsRepository.findByTitle(news.getTitle()).get();
        //Assertions
        Assertions.assertNotNull(returnNews);
        Assertions.assertEquals(news,returnNews,"this two objects must be equals");
    }
    // Update
    @Test
    public void NewsRepository_NewsUpdate_ReturnNews()
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



        newsRepository.save(news);
        // Act
        News returnNews = newsRepository.findById(news.getId()).get();

        returnNews.setDescription("Changed description");
        returnNews.setTitle("Changed title");

        News updatedNews= newsRepository.save(returnNews);
        //Assertions
        Assertions.assertNotNull(updatedNews.getDescription());
        Assertions.assertNotNull(updatedNews.getTitle());

        Assertions.assertEquals("Changed description",updatedNews.getDescription());
        Assertions.assertEquals("Changed title",updatedNews.getTitle());
    }
    // Delete
    @Test
    public void NewsRepository_NewsDelete_ReturnNull()
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
        newsRepository.save(news);

        //Act
        newsRepository.deleteById(news.getId());
        Optional<News> returnedNews = newsRepository.findById(news.getId());
        Assertions. assertFalse(returnedNews.isPresent(), "Returned news should be empty after deletion"); // assertFalse - used to check if the condition is false , isPresent method - returns true if there is a value in the object optional
    }

}
