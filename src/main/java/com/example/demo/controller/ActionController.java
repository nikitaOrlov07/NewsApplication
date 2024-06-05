package com.example.demo.controller;

import com.example.demo.DTO.NewsDto;
import com.example.demo.mappers.NewsMapper;
import com.example.demo.models.Comment;
import com.example.demo.models.security.UserEntity;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.News;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ActionController {
    private final CommentService commentService;
    private final NewsService newsService;
    private final UserService userService;

    public ActionController(CommentService commentService, NewsService newsService, UserService userService) {
        this.commentService = commentService;
        this.newsService = newsService;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ActionController.class);

    @PostMapping("/comments/{newsId}/save")
    public String addComment(@ModelAttribute("comments") Comment comment, @PathVariable("newsId") Long newsId, RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        UserEntity user = userService.findByUsername(username);
        user.getComments().add(comment);

        // For current date time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = currentDateTime.format(formatter);
        comment.setPubDate(formattedDateTime);

        comment.setUser(user);


        commentService.saveComment(comment, newsId);
        return "redirect:/news/" + newsId;
    }

    @PostMapping("/comments/{newsId}/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("newsId") Long newsId) {
        // Take comment from database and delete it with service method and from users comment list
        Comment comment = commentService.findCommentById(commentId);
        UserEntity user = userService.findById(comment.getUser().getId());
        user.getComments().remove(comment);
        commentService.deleteComment(comment, newsId);
        return "redirect:/news/" + newsId;
    }


    // like and dislike for news
    @PostMapping("/news/actions/{newsId}")
    public String actionLogic(@PathVariable("newsId") Long newsId,
                              @RequestParam(value = "interaction") String interaction,
                              RedirectAttributes redirectAttributes) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) // if the user is not authorized
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        News news = newsService.getNewsById(newsId);
        if (interaction != null) {
            switch (interaction) {
                case "like":
                    userService.actionNews("like", news, "news", null);
                    break;
                case "dislike":
                    userService.actionNews("dislike", news, "news", null);
                    break;
            }
        } else {
            logger.error("Interaction variable is null");
        }

        newsService.updateNews(news);
        return "redirect:/news/" + newsId; // redirect to news detail page
    }

    // like and dislike for comments
    @PostMapping("/news/actions/{newsId}/comments/{commentId}")
    public String commentActionLogic(@PathVariable("newsId") Long newsId, @PathVariable("commentId") Long commentId, @RequestParam(value = "interaction") String interaction,
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
                    userService.actionNews("like", null, "comment", comment);
                    break;
                case "dislike":
                    userService.actionNews("dislike", null, "comment", comment);
                    break;
            }
        } else {
            logger.error("Interaction variable is null");
        }

        return "redirect:/news/" + newsId; // redirect to news detail page)
    }
    // Admin can delete user
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        UserEntity userToDelete = userService.findById(userId);
        if(user.hasAdminRole())
        {
            userService.deleteUserById(userId);
            return "redirect:/cabinet";
        }
        else if(userToDelete.equals(userToDelete))
        {
            userService.deleteUserById(userId);
            return "redirect:/logout";
        }
        else
            return "redirect:/news";
    }
    // user cabinet (and list of all users (only for admin))
    @GetMapping("/cabinet")
    public String userCabinet(Model model)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        model.addAttribute("user", user);
        if(user.hasAdminRole())
        {
            List<UserEntity> users= userService.findAllUsers();
            model.addAttribute("users",users);
        }

        return "personal-cabinet";
    }
    // User search (only for admin)
    @GetMapping("/users/find")
    public String searchUser(@RequestParam(value="query",defaultValue = " ") String query,Model model)
    {
        if(!userService.findByUsername(SecurityUtil.getSessionUser()).hasAdminRole())
        {
            return "redirect:/news";
        }
        model.addAttribute("users",userService.searchUser(query));
        logger.info("search logic are working");
        return "personal-cabinet :: userList"; // userList is a fragment
    }
}
