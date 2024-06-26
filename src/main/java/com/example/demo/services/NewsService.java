package com.example.demo.services;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;

import java.sql.Date;
import java.util.List;

public interface NewsService {
    List<ApiResponse> getNewsFromApi();
    News getNewsById(long id);

    void saveIfNotExists(List<News> newsList);
    NewsPagination getAllNews(int pageNo, int pageSize);
    NewsPagination getNewsByLanguageAndCategoryAndQueryAndPubDate(String language, String category,  String pubDate, String query, String sort, int pageNo, int pageSize);
    News updateNews(News news);

    void deleteNews(News news);

    News createNews(NewsDto newsDto);
}
