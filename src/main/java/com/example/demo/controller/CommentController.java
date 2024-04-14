package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;

    @PostMapping("/comments/{newsId}/save")
    public String addComment(@ModelAttribute("comments") Comment comment , @PathVariable("newsId") Long newsId) {
        String username = SecurityUtil.getSessionUser();
        comment.setNews(newsService.getNewsById(newsId));

        comment.setAuthor(username);
        commentService.saveComment(comment);
        return "redirect:/news/"+newsId;
    }
}
