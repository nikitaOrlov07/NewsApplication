package com.example.demo.controller;
import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController{
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsRepository newsRepository;
     // https://newsdata.io/api-key - how many times we execute project
    @GetMapping("/news")
    public String mainPage(Model model,
                           @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                           @RequestParam(value="pageSize", defaultValue="6",required=false) int pageSize)
    {
        NewsPagination news_list =  newsService.getAllNews(pageNo,pageSize);
        model.addAttribute("news", news_list); // for just data


        return "home-page";
    }
    //Detail page
    @GetMapping("/news/{newsId}")
    public String detailPage(@PathVariable("newsId") long newsId, Model model)
    {
        News news = newsService.getNewsById(newsId);
        List<Comment> comments_list = commentService.getComments(newsId);
        model.addAttribute("comments", comments_list);
        model.addAttribute("news", news);
        return "detail-page";
    }
    // Category
    @GetMapping("/news/category/{category}")
    public String sportPage(Model model,@PathVariable("category") String category)
    {
        List<News> news_list = newsService.getNewsByCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("news_list", news_list);

        return "category-news";
    }
    // Language
    @GetMapping("/news/language/{language}")
    public String languagePage(Model model,@PathVariable("language") String language)
    {
        List<News> news_list = newsService.getNewsByLanguage(language);

        model.addAttribute("category", language);
        model.addAttribute("news_list", news_list);

        return "language-news";
    }
    @GetMapping("/news/search")
    public String searchClub(@RequestParam(value = "query") String query , Model model,
                             @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                             @RequestParam(value="pageSize", defaultValue="6",required=false) int pageSize)
    {
        NewsPagination news= newsService.searchNews(query,pageNo,pageSize);

        model.addAttribute("news",news);
        return "home-page";
    }
}
