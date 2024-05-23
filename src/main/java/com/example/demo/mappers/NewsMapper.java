package com.example.demo.mappers;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.Result;
import com.example.demo.models.News;

import java.util.ArrayList;
import java.util.Collections;
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
    public static News getNewsFromDto(NewsDto newsDto)
    {
        News news = News.builder()
                .id(newsDto.getId())
                .title(newsDto.getTitle())
                .imageUrl(newsDto.getImageUrl())
                .description(newsDto.getDescription())
                .country(Collections.singletonList(newsDto.getCountry()))
                .build(); //методы builder и build добавились к club
        return  news;
    }

}
