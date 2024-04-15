package com.example.demo.services.impl;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.News;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class NewsServiceimpl implements NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    CommentRepository commentRepository;
    @Override
    public List<ApiResponse> getNewsFromApi() {
        String url = "https://newsdata.io/api/1/news?apikey=pub_416437263d95d7cebaaae9cac7ade7143a7e1";

        WebClient.Builder builder = WebClient.builder();
        Flux<ApiResponse> responseFlux = builder.build()
                .get()
                .uri(url)
                .retrieve().bodyToFlux(ApiResponse.class);
        return responseFlux.toStream().toList();
    }

    @Override
    public News getNewsById(long id) {
        return newsRepository.findById(id).get();
    }

    @Override
    public void deleteAllFromDatabase() {

        newsRepository.deleteAll();
    }

    @Override
    public void saveNewDataInDatabase(List<News> news) {
        newsRepository.saveAll(news);
    }

    @Override
    public List<News> getNewsByCategory(String category) {
        return newsRepository.getNewsByCategory(category);
    }

    @Override
    public List<News> getNewsByLanguage(String language) {
        return newsRepository.getNewsByLanguage(language);
    }


    @Override
    public NewsPagination getAllNews(int pageNo, int pageSize) {
        deleteAllFromDatabase();
        saveNewDataInDatabase(NewsMapper.apiResponseToNews(getNewsFromApi()));

        Pageable pageable = PageRequest.of(pageNo,pageSize); // define information about pagination
        Page<News> news=newsRepository.findAll(pageable);
        List<News> newsList= news.getContent();

        NewsPagination newsPagination = new NewsPagination();
        newsPagination.setData(newsList);
        newsPagination.setPageNo(news.getNumber());
        newsPagination.setPageSize(news.getSize());
        newsPagination.setTotalElements(news.getTotalElements());
        newsPagination.setTotalPages(news.getTotalPages());
        newsPagination.setLast(news.isLast());

       return newsPagination;
    }

    @Override
    public NewsPagination searchNews(String query,int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize); // define information about pagination

        NewsPagination newsPagination = new NewsPagination();
        Page<News> news= newsRepository.searchNews(query,pageable);

        List<News> newsList = news.getContent();

        newsPagination.setData(newsList);
        newsPagination.setPageNo(news.getNumber());
        newsPagination.setPageSize(news.getSize());
        newsPagination.setTotalElements(news.getTotalElements());
        newsPagination.setTotalPages(news.getTotalPages());
        newsPagination.setLast(news.isLast());
        return newsPagination;
    }
}
