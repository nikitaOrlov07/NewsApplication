package com.example.demo.repository;


import com.example.demo.models.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Long> {
List<News> getNewsByCategory(String category);
List<News> getNewsByLanguage(String language);
@Query("Select c from News c WHERE c.title LIKE CONCAT('%', :query ,'%')")
Page<News> searchNews(String query, Pageable pageable);
}

