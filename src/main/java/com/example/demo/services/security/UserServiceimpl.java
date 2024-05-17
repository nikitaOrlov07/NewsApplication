package com.example.demo.services.security;

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
    public void actionNews(String action, News news) {
        if(action != null && !action.isEmpty())
        {
            String userName = SecurityUtil.getSessionUser();
            UserEntity user =  userRepository.findByUsername(userName);
           if(action.equals("like") && !user.getLikedNews().contains(news))
           {
               news.setLikes(news.getLikes() + 1);
               user.getLikedNews().add(news);
               userRepository.save(user);
           }
           if(action.equals("dislike") && !user.getDislikedNews().contains(news))
           {
               news.setDislikes(news.getDislikes() + 1);
               user.getDislikedNews().add(news);
               userRepository.save(user);
           }
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

}
