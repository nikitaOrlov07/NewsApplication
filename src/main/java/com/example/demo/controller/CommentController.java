package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.models.security.UserEntity;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.News;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsRepository repository;
    @Autowired
    CommentRepository repository2;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @PostMapping("/comments/{newsId}/save")
    public String addComment(@ModelAttribute("comments") Comment comment , @PathVariable("newsId") Long newsId) {

        commentService.saveComment(comment,newsId);
        return "redirect:/news/"+newsId;
    }

    @PostMapping("/comments/{newsId}/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("newsId") Long newsId) {
        // Take comment grom database and delete it with service method
        Comment comment = repository2.findCommentById(commentId);
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
            redirectAttributes.addFlashAttribute("loginError", "You must login");
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
            redirectAttributes.addFlashAttribute("loginError", "You must login");
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

}
