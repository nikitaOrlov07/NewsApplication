package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.News;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class ActionController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ActionController.class);
    @PostMapping("/comments/{newsId}/save")
    public String addComment(@ModelAttribute("comments") Comment comment , @PathVariable("newsId") Long newsId, RedirectAttributes redirectAttributes  ) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        commentService.saveComment(comment,newsId);
        return "redirect:/news/"+newsId;
    }

    @PostMapping("/comments/{newsId}/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("newsId") Long newsId) {
        // Take comment grom database and delete it with service method
        Comment comment = commentService.findCommentById(commentId);
        commentService.deleteComment(comment,newsId);

        return "redirect:/news/" + newsId;
    }


    // like and dislike for news
    @PostMapping("/news/actions/{newsId}")
    public String actionLogic(@PathVariable("newsId") Long newsId,
                            @RequestParam(value ="interaction") String interaction,
                            RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        News news = newsService.getNewsById(newsId);
        if(interaction != null) {
            switch (interaction) {
                case "like":
                    userService.actionNews("like",news,"news",null);
                    break;
                case "dislike":
                    userService.actionNews("dislike",news,"news",null);
                    break;
            }
        }
        else {logger.error("Interaction variable is null");}

        newsService.updateNews(news);
        return "redirect:/news/" + newsId; // redirect to news detail page
    }

    // like and dislike for comments
    @PostMapping("/news/actions/{newsId}/comments/{commentId}")
    public String commentActionLogic(@PathVariable("newsId") Long newsId, @PathVariable("commentId") Long commentId,@RequestParam(value ="interaction") String interaction,
                                     RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }

        Comment comment = commentService.findCommentById(commentId);
        if (interaction != null) {
            switch (interaction) {
                case "like":
                    userService.actionNews("like", null,"comment",comment);
                    break;
                case "dislike":
                    userService.actionNews("dislike", null,"comment",comment);
                    break;
            }
        }
        else {
            logger.error("Interaction variable is null");
        }

        return "redirect:/news/" + newsId; // redirect to news detail page)
    }

    // Admin can create news
    @GetMapping("/news/create")
    public String addNews(Model model)
    {
        String username = SecurityUtil.getSessionUser();
        if (username == null || !userService.findByUsername(username).hasAdminRole()) // if the user is not authorized
        {
            return "redirect:/news";
        }
        News news = new News();
        model.addAttribute("news", news);
        return "create-news";
    }
    @PostMapping("/news/create/save")
    public String addNews(@ModelAttribute("news") News news )
    {
        String username = SecurityUtil.getSessionUser();
        if (username == null || !userService.findByUsername(username).hasAdminRole()) // if the user is not authorized
        {
            return "redirect:/news";
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        // Formátovač na konkrétny formát dátumu a času
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = currentDateTime.format(formatter);
        news.setPubdate(formattedDateTime.toString());
        newsService.updateNews(news);
        return "redirect:/news";
    }
    // Admin can update news
    @GetMapping("/news/update/{newsId}")
    public String updateNews(Model model, @PathVariable("newsId") Long newsId)
    {
        String username = SecurityUtil.getSessionUser();
        if (username == null || !userService.findByUsername(username).hasAdminRole()) // if the user is not authorized
        {
            return "redirect:/news";
        }
        News news = newsService.getNewsById(newsId);
        model.addAttribute("news", news);
        return "update-news";
    }
    @PostMapping("/news/update/save")
    public String updateNews(@ModelAttribute("news") News news)
    {
        String username = SecurityUtil.getSessionUser();
        if (username == null || !userService.findByUsername(username).hasAdminRole()) // if the user is not authorized
        {
            return "redirect:/news";
        }

        newsService.updateNews(news);
        return "redirect:/news";
    }


}
