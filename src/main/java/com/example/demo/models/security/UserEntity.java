package com.example.demo.models.security;


import com.example.demo.models.Comment;
import jakarta.persistence.*;
import lombok.*;
import com.example.demo.models.News;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String town;
    private Long phoneNumber;
    private int roleId; //{0,1}
    String creationDate;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_role",joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )// с помощью этой аннотации Spring сам создаст Join- таблицу
    private List<RoleEntity> roles = new ArrayList<>(); // список ролей для данного пользователя. Каждый пользователь может иметь список ролей.
    public boolean hasAdminRole() {
        for (RoleEntity role : roles) {
            if (role.getName().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_seen_news",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "news_id", referencedColumnName = "id")}
    )
    private List<News> seenNews = new ArrayList<>();

    // actions with news
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_liked_news",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "news_id", referencedColumnName = "id")}
    )
    private List<News> likedNews = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_disliked_news",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "news_id", referencedColumnName = "id")}
    )
    private List<News> dislikedNews = new ArrayList<>();

    // actions with comments

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_liked_comments",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "comment_id", referencedColumnName = "id")}
    )
    private List<Comment> likedComments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_disliked_comments",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "comment_id", referencedColumnName = "id")}
    )
    private List<Comment> dislikedComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // one user --> many comments in comment side i have @ ManyToone annotation
    private List<Comment> comments = new ArrayList<>();


}
