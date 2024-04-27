package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
