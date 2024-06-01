package com.example.demo.mappers;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.Result;
import com.example.demo.models.News;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.time.LocalDateTime;


public class NewsMapper {
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<News> apiResponseToNews(List<ApiResponse> responses) {
        List<News> newsList = new ArrayList<>();
        for (ApiResponse response : responses) {
            for (Result result : response.getResult()) {

                LocalDateTime pubdateTime = LocalDateTime.parse(result.getPubdate(), INPUT_DATE_FORMATTER);
                String formattedPubdate = pubdateTime.format(OUTPUT_DATE_FORMATTER);

                News news = News.builder()
                        .imageUrl(result.getImageUrl())
                        .videoUrl(result.getVideoUrl())
                        .creator(result.getCreator())
                        .description(result.getDescription())
                        .pubdate(formattedPubdate)
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

    public static News getNewsFromDto(NewsDto newsDto) {
        News news = News.builder()
                .id(newsDto.getId())
                .title(newsDto.getTitle())
                .imageUrl(newsDto.getImageUrl())
                .description(newsDto.getDescription())
                .country(newsDto.getCountry()) // Collections.singletonList - create immutable list
                .build();
        return news;
    }
    public static NewsDto getNewsDtoFromNews(News news) {
        NewsDto newsDto = NewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .imageUrl(news.getImageUrl())
                .description(news.getDescription())
                .country(news.getCountry())
                .build();
        return newsDto;
    }
}
