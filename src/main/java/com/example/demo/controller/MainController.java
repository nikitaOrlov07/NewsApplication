package com.example.demo.controller;
import com.example.demo.DTO.ApiResponse;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.impl.NewsServiceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
public class MainController{
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;
     // https://newsdata.io/api-key - how many times we execute project
     private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/news")
    public String mainPage(Model model,
                           @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                           @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize)
    {
        NewsPagination news_list =  newsService.getAllNews(pageNo,pageSize);
        model.addAttribute("news", news_list);

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

    @GetMapping("/news/find") // this controller-method will handle two url
public String categoryAndLanguagePage(Model model,
                                      @RequestParam(value ="language",required = false) String language,
                                      @RequestParam(value ="category",required = false) String category,
                                      @RequestParam(value="query",required = false) String query,
                                      @RequestParam(value="pubDate",required = false)  String date,
                                      @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                                      @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize) // "required=false"--> если переменная отсутствует в URL, то Spring MVC не сгенерирует исключение MissingPathVariableException
{

    NewsPagination news_list = newsService.getNewsByLanguageAndCategoryAndQueryAndPubDate(language,category, date,query,pageNo,pageSize);


    if(news_list == null || news_list.getData().isEmpty()){
        logger.error("News List is empty");
    }

    logger.error("PageNo is "+news_list.getPageNo());
    logger.info("Date is "+date + "Date class is: "+ date);

    model.addAttribute("language", language);
    model.addAttribute("category", category);
    model.addAttribute("news", news_list);
    try {
        model.addAttribute("pub-date", date.toString());
    }catch (NullPointerException e){logger.error("pub-date is null");}
    model.addAttribute("query",query);
    return "home-page";
}
}
