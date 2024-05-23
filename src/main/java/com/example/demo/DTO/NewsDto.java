package com.example.demo.DTO;

import com.example.demo.models.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String imageUrl;
    String videoUrl;
    @NotEmpty(message = "You must enter Description")
    String description;
    @NotEmpty(message = "You must enter title")
    String title;
    String language;
    String pubdate;
    @NotEmpty(message = "You must enter country")
    String country;

}
