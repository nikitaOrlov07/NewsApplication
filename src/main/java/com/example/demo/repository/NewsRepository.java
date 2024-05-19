package com.example.demo.repository;


import com.example.demo.models.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News,Long> {
// find news by different oprions
       // have only one parametr
     Page<News> getNewsByCategory(String category,Pageable pageable);
     Page<News> getNewsByLanguage(String language,Pageable pageable);
     Page<News> getNewsByPubdate( String date, Pageable pageable);
     // have two parameters
     Page<News >findByLanguageAndCategory(String language,String category,Pageable pageable);
     Page<News> findByLanguageAndPubdate(String language, String pubDate,Pageable pageable);
     Page<News> findByPubdateAndCategory( String pubDate, String category,Pageable pageable);
     // have three parameters
     Page<News> findByLanguageAndCategoryAndPubdate(String language, String category, String pubDate,Pageable pageable);

     @Query("Select c from News c WHERE c.title LIKE CONCAT('%', :query ,'%')")
     Page<News> searchNews(String query, Pageable pageable);
     Optional<News> findByTitle(String title);

     // for finding top news by views count
     Page<News> findAllByOrderByPageVisitingCountDesc(Pageable pageable); //desc = descending sort = "по убыванию"
     Page<News> findAllByOrderByLikesDesc(Pageable pageable);
}

