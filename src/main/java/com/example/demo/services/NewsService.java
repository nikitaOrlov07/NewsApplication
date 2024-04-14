package com.example.demo.services;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.models.News;

import java.util.List;

public interface NewsService {
    List<ApiResponse> getNewsFromApi();
    News getNewsById(long id);
    void deleteAllFromDatabase();
    void saveNewDataInDatabase(List<News> news);
    List<News>  getNewsByCategory(String category);
    List<News> getNewsByLanguage(String language);
    NewsPagination getAllNews(int pageNo, int pageSize);
    List<News> searchNews(String query);
}
