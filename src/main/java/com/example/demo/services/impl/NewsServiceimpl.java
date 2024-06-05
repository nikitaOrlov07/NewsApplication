package com.example.demo.services.impl;

import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.security.UserRepository;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NewsServiceimpl implements NewsService {

    private NewsRepository newsRepository; private CommentService commentService; private UserService userService;

    @Autowired
    public NewsServiceimpl(NewsRepository newsRepository, CommentService commentService, UserService userService) {
        this.newsRepository = newsRepository;
        this.commentService = commentService;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceimpl.class);
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
        return newsRepository.findById(id).orElse(null);
    }

    // method for saving non-existed news from Api
    @Override
    public void  saveIfNotExists(List<News> newsList) {

        List<News> savedNews = new ArrayList<>();
        for (News news : newsList) {
            News existingNews = newsRepository.findByTitle(news.getTitle()).orElse(null); //if this news is already exist in the database -> write this news into a variable
            if (existingNews == null) {
                savedNews.add(newsRepository.save(news));
            }
        }
    }

    @Override
    public NewsPagination getAllNews(int pageNo, int pageSize) {

        // check for error with saving non-existed data that I got from Api


        /*try {
            saveIfNotExists(NewsMapper.apiResponseToNews(getNewsFromApi()));
        }

        catch (Exception e)
        {
          logger.error("problem with api response");
        }

         */

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
    public NewsPagination getNewsByLanguageAndCategoryAndQueryAndPubDate(String language, String category,  String pubDate, String query,String sort, int pageNo, int pageSize) {
        Page<News> news= null;

        Sort sort_object = Sort.unsorted(); // object, which is used to define the sort order of data when executing database queries. In this line we create object without sorting

        if (sort != null) {
            if( sort.equals("views")) {
                sort_object = Sort.by(Sort.Direction.DESC, "pageVisitingCount"); // if sorts = views , we override Sort object and add sorting by "pageVisitingCount"  variable
            }
            else if( sort.equals("likes"))
            {
                sort_object=Sort.by(Sort.Direction.DESC, "likes");
            }
            else if( sort.equals("comments"))
            {
                sort_object=Sort.by(Sort.Direction.DESC, "commentsCount");
            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort_object);// define information about pagination
        if(query != null) {
            news = newsRepository.searchNews(query, pageable);
        }

        // if we don`t have query
        if((news == null || news.isEmpty()) && (category != null || language != null || pubDate != null )) {
            if(category != null && language!=null && pubDate != null) {
                news = newsRepository.findByLanguageAndCategoryAndPubdate(language, category, pubDate, pageable);
            } else if(category != null && language!=null) {
                news = newsRepository.findByLanguageAndCategory(language, category, pageable);
            } else if(category != null && pubDate != null) {
                news = newsRepository.findByPubdateAndCategory(pubDate, category, pageable);
            } else if(language != null && pubDate != null) {
                news = newsRepository.findByLanguageAndPubdate(language, pubDate, pageable);
            } else if(category != null) {
                news = newsRepository.getNewsByCategory(category, pageable);
            } else if(language != null) {
                news = newsRepository.getNewsByLanguage(language, pageable);
            } else if(pubDate != null) {
                news = newsRepository.getNewsByPubdate(pubDate, pageable);
            }
        }
        // if query , category, language, pubDate --> were empty --> we use only sorting
        if(news == null)
        {
          pageable = PageRequest.of(pageNo,pageSize);
          if(sort!=null ) {
              if (sort.equals("views")) {
                  news = newsRepository.findAllByOrderByPageVisitingCountDesc(pageable);
              } else if (sort.equals("likes")) {
                  news = newsRepository.findAllByOrderByLikesDesc(pageable);
              } else if (sort.equals("comments")) {
                  news= newsRepository.findAllByOrderByCommentsCountDesc(pageable);
              }
          }
        }
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
    private String getCurrentTime()
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDateTime = currentDateTime.format(formatter);
        return  formattedDateTime;
    }
    @Override
    public News updateNews(News news) {
        System.out.println();
        news.setPubdate(getCurrentTime());
        return newsRepository.save(news);}
    @Override
    public void deleteNews(News news) {

            // for seen News
            List<UserEntity> users = userService.findAllBySeenNews(news);
            if(users != null && !users.isEmpty()) {
                for (UserEntity user : users) {
                    user.getSeenNews().remove(news);
                }
            }
            // for liked news
            users = userService.findAllByLikedNews(news);
            if(users != null && !users.isEmpty()) {
            for (UserEntity user : users) {
                user.getLikedNews().remove(news);
                 }
            }
            // for disliked news
            users =userService.findAllByDislikedNews(news);
            if(users != null && !users.isEmpty()) {
            for (UserEntity user : users) {
                user.getLikedNews().remove(news);
               }
            }

        // Remove comments associated with the news
        List<Comment> comments = commentService.getComments(news.getId());
        if(comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
                // Remove comment from users who liked the comment
                List<UserEntity> usersWhoLikedComment = userService.findAllByLikedComments(comment);
                if(usersWhoLikedComment != null && !usersWhoLikedComment.isEmpty()) {
                    for (UserEntity user : usersWhoLikedComment) {
                        user.getLikedComments().remove(comment);
                        userService.updateUser(user);
                    }
                }

                // Remove comment from users who disliked the comment
                List<UserEntity> usersWhoDislikedComment = userService.findAllByDislikedComments(comment);
                if(usersWhoDislikedComment != null && !usersWhoDislikedComment.isEmpty()) {
                    for (UserEntity user : usersWhoDislikedComment) {
                        user.getDislikedComments().remove(comment);
                        userService.updateUser(user);
                    }
                }

                // Remove comment from all users comments lists
                List<UserEntity> usersWithComment = userService.findAllByComments(Arrays.asList(comment));
                if(usersWithComment != null && !usersWithComment.isEmpty()) {
                    for (UserEntity user : usersWithComment) {
                        user.getComments().remove(comment);
                        userService.updateUser(user);
                    }
                }
                commentService.deleteComment(comment, news.getId());
            }
        }
         newsRepository.delete(news);
    }

    @Override
    public News createNews(NewsDto newsDto) { newsDto.setPubdate(getCurrentTime()); return newsRepository.save(NewsMapper.getNewsFromDto(newsDto)); // for test
    }

}


