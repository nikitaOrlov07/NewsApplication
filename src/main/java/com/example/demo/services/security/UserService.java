package com.example.demo.services.security;


import com.example.demo.DTO.security.RegistrationDto;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
    List<UserEntity> findAllUsers();

    UserEntity findById(Long userId);
    void actionNews(String action, News news, String type, Comment comment);

    void updateNewsList(News news);
     void updateUser(UserEntity user);
}
