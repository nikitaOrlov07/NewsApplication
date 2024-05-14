package com.example.demo.services;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.models.News;

import java.sql.Date;
import java.util.List;

public interface NewsService {
    List<ApiResponse> getNewsFromApi();
    News getNewsById(long id);

    void saveIfNotExists(List<News> newsList);
    NewsPagination getAllNews(int pageNo, int pageSize);
    NewsPagination getNewsByLanguageAndCategoryAndQueryAndPubDate(String language, String category,  String pubDate, String query, int pageNo, int pageSize);
    void updateNews(News news);

}
