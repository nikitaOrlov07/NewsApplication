package com.example.demo.services.security;

import com.example.demo.controller.CommentController;
import com.example.demo.models.News;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.security.UserService;
import com.example.demo.DTO.security.RegistrationDto;
import com.example.demo.models.Comment;
import com.example.demo.models.security.RoleEntity;
import com.example.demo.models.security.UserEntity;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.security.RoleRepository;
import com.example.demo.repository.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceimpl implements UserService {
    private UserRepository userRepository; private RoleRepository roleRepository;  // implements methods from repositories
    private PasswordEncoder passwordEncoder; private NewsRepository newsRepository; private CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);

    @Autowired
    public UserServiceimpl(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder, NewsRepository newsRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.newsRepository = newsRepository;
        this.commentRepository =commentRepository;
    } // Technically we don`t need a constructor , but it is good practice

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity userEntity = new UserEntity(); // we cant save RegistrationDto to the database because it`s totally different entity
        // create something like mapper
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userEntity.setTown(registrationDto.getTown());
        userEntity.setPhoneNumber(registrationDto.getPhoneNumber());

        RoleEntity role = roleRepository.findByName("USER");// по факту "USER"  записывается в переменную role (- В этой строке мы ищем объект RoleEntity, представляющий роль "USER" в системе.)
        userEntity.setRoles(Arrays.asList(role));// даем нашему userEntity (юзеру) роль "USER" (мы назначаем найденную роль "USER" пользователю, устанавливая список ролей пользователя в качестве списка,
        // содержащего только одну роль "USER".)
        //----------------------------------------------------------------
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }
    @Override
    public UserEntity findById(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    @Override
    public void updateNewsList(News news) {
        String userName = SecurityUtil.getSessionUser();
        UserEntity user =  userRepository.findByUsername(userName);

        if (!user.getSeenNews().contains(news)) {
            user.getSeenNews().add(news);
            userRepository.save(user);
        }
    }

    @Override
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void actionNews(String action, News news, String type,Comment comment) {
        if(action != null && !action.isEmpty())
        {
            String userName = SecurityUtil.getSessionUser();
            UserEntity user =  userRepository.findByUsername(userName);
            if(type.equals("news") || news != null)
            {
           if(action.equals("like"))
           {
               if(!user.getLikedNews().contains(news) && !user.getDislikedNews().contains(news)) {     // if user did not like this news
                   news.setLikes(news.getLikes() + 1);
                   logger.info("User did not like this news");
                   user.getLikedNews().add(news);
                   userRepository.save(user);
               }
               else if(user.getLikedNews().contains(news) && !user.getDislikedNews().contains(news)) { // if user liked this news before
                   news.setLikes(news.getLikes() - 1);
                   logger.info("User liked this news");
                   user.getLikedNews().remove(news);
                   userRepository.save(user);
               }
               else if(user.getDislikedNews().contains(news))                                          // if user disliked this news before
               {
                   // remove this news from disliked news
                   news.setDislikes(news.getDislikes() - 1);
                   user.getDislikedNews().remove(news);
                   logger.info("remove user dislike");
                   // Add this news to liked news
                   news.setLikes(news.getLikes() + 1);
                   user.getLikedNews().add(news);
                   logger.info("add user like");
                   userRepository.save(user);
               }
           }
           else if(action.equals("dislike"))
           {

               if(!user.getDislikedNews().contains(news) && !user.getLikedNews().contains(news)) {
                   news.setDislikes(news.getDislikes() + 1);
                   user.getDislikedNews().add(news);
                   logger.info("User did not dislike this news");
                   userRepository.save(user);
               }
               else if(user.getDislikedNews().contains(news) && !user.getLikedNews().contains(news))
               {
                   news.setDislikes(news.getDislikes() - 1);
                   user.getDislikedNews().remove(news);
                   logger.info("User disliked this news");
                   userRepository.save(user);
               }
               else if(user.getLikedNews().contains(news)) // if user liked this news этот метод не происходит
               {
                   news.setLikes(news.getLikes() - 1);
                   user.getLikedNews().remove(news);
                   logger.info("remove user like");
                   news.setDislikes(news.getDislikes() + 1);
                   user.getDislikedNews().add(news);
                   logger.info("add user dislike");
                   userRepository.save(user);
               }
           }
        }

            // the same but for comments
            else if(type.equals("comment"))
            {
                if(action.equals("like"))
                {
                    if(!user.getLikedComments().contains(comment) && !user.getDislikedComments().contains(comment)) {     // if user did not like this news +
                        comment.setLikes(comment.getLikes() + 1);
                        logger.info("User did not like this comment");
                        user.getLikedComments().add(comment);
                        userRepository.save(user);
                    }
                    else if(user.getLikedComments().contains(comment) && !user.getDislikedComments().contains(comment)) { // if user liked this news before
                        comment.setLikes(comment.getLikes() - 1);
                        logger.info("User liked this news");
                        user.getLikedComments().remove(comment);
                        userRepository.save(user);
                    }
                    else if(user.getDislikedComments().contains(comment))                                          // if user disliked this news before
                    {
                        // remove this news from disliked news
                        comment.setDislikes(comment.getDislikes() - 1);
                        user.getDislikedComments().remove(comment);
                        logger.info("remove user dislike");
                        // Add this news to liked news
                        comment.setLikes(comment.getLikes() + 1);
                        user.getLikedComments().add(comment);
                        logger.info("add user like");
                        userRepository.save(user);
                    }
                }
                else if(action.equals("dislike"))
                {

                    if(!user.getDislikedComments().contains(comment) && !user.getLikedComments().contains(comment)) {
                        comment.setDislikes(comment.getDislikes() + 1);
                        user.getDislikedComments().add(comment);
                        logger.info("User did not dislike this news");
                        userRepository.save(user);
                    }
                    else if(user.getDislikedComments().contains(comment) && !user.getLikedComments().contains(comment))
                    {
                        comment.setDislikes(comment.getDislikes() - 1);
                        user.getDislikedComments().remove(comment);
                        logger.info("User disliked this news");
                        userRepository.save(user);
                    }
                    else if(user.getLikedComments().contains(comment)) // if user liked this news этот метод не происходит
                    {
                        comment.setLikes(comment.getLikes() - 1);
                        user.getLikedComments().remove(comment);
                        logger.info("remove user like");
                        comment.setDislikes(comment.getDislikes() + 1);
                        user.getDislikedComments().add(comment);
                        logger.info("add user dislike");
                        userRepository.save(user);
                    }
                }

            }
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }


}
