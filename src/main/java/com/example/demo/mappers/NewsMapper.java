package com.example.demo.mappers;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.Result;
import com.example.demo.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsMapper {
    public static List<News> apiResponseToNews(List<ApiResponse> responses) {
        List<News> newsList = new ArrayList<>();
        for (ApiResponse response : responses) {
            for (Result result : response.getResult()) {
                News news = News.builder()
                        .imageUrl(result.getImageUrl())
                        .videoUrl(result.getVideoUrl())
                        .creator(result.getCreator())
                        .description(result.getDescription())
                        .pubdate(result.getPubdate())
                        .country(result.getCountry())
                        .title(result.getTitle())
                        .category(result.getCategory())
                        .language(result.getLanguage())
                        .build();
                newsList.add(news);
            }
        }
        return newsList;
    }

}
