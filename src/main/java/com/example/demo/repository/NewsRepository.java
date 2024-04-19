package com.example.demo.repository;


import com.example.demo.models.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News,Long> {
Page<News> getNewsByCategory(String category,Pageable pageable);
Page<News> getNewsByLanguage(String language,Pageable pageable);

@Query("Select c from News c WHERE c.title LIKE CONCAT('%', :query ,'%')")
Page<News> searchNews(String query, Pageable pageable);
Optional<News> findByTitle(String title);
Page<News> findByLanguageAndCategory(String language, String category,Pageable pageable);
}

