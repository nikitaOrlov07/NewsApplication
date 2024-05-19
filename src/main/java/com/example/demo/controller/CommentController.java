package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.models.security.UserEntity;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.models.News;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @PostMapping("/comments/{newsId}/save")
    public String addComment(@ModelAttribute("comments") Comment comment , @PathVariable("newsId") Long newsId) {
        String username = SecurityUtil.getSessionUser();
        comment.setNews(newsService.getNewsById(newsId));

        comment.setAuthor(username);
        commentService.saveComment(comment);
        return "redirect:/news/"+newsId;
    }

    // like and dislike for news
    @PostMapping("/news/actions/{newsId}")
    public String likeLogic(@PathVariable("newsId") Long newsId,
                            @RequestParam(value ="interaction") String interaction,
                            RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must login");
            return "redirect:/login";
        }
        News news = newsService.getNewsById(newsId);
        if(interaction != null) {
            switch (interaction) {
                case "like":
                    userService.actionNews("like",news);
                    break;
                case "dislike":
                    userService.actionNews("dislike",news);
                    break;
            }
        }
        else {logger.error("Interaction variable is null");}

        newsService.updateNews(news);
        return "redirect:/news/" + newsId; // redirect to news detail page
    }

    // like and dislike for comments

}
