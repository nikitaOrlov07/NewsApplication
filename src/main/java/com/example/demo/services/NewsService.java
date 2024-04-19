package com.example.demo.services;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.models.News;

import java.util.List;

public interface NewsService {
    List<ApiResponse> getNewsFromApi();
    News getNewsById(long id);
    void deleteAllFromDatabase();
    void saveIfNotExists(List<News> newsList);
    NewsPagination getNewsByCategory(String category,int pageNo,int pageSize);
    NewsPagination getNewsByLanguage(String language,int pageNo,int pageSize) ;
    NewsPagination getAllNews(int pageNo, int pageSize);
    NewsPagination searchNews(String query,int pageNo, int pageSize);
    NewsPagination getNewsByLanguageAndCategory(String language, String category,int pageNo, int pageSize);
}
