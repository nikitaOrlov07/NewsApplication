package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition = "TEXT")
    String imageUrl;
    @Column(columnDefinition = "TEXT")
    String videoUrl;
    @ElementCollection
    List<String> creator;
    @Column(columnDefinition = "TEXT") // databases column type for this attribute
    String description;

    @Column(columnDefinition = "TEXT") // databases column type for this attribute
    String title;
    String language;
    String pubdate;

    @ElementCollection
    List<String> country;
    @ElementCollection
    List<String> category;
    // for "views"
    int pageVisitingCount = 0;
    // for "likes and dislikes"
    int likes = 0;
    int dislikes = 0;
    // for "comments count

    @ToString.Exclude
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Comment> comments;

    int commentsCount = 0;
}
