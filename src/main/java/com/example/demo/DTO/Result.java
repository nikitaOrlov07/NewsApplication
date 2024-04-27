package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;
import java.util.List;



@Getter
@JsonIgnoreProperties(ignoreUnknown = true)// ignore unspecify properties
public class Result {

    // Spring will set variables from json file
    @JsonProperty("image_url")
    String imageUrl;
    @JsonProperty("video_url")
    String videoUrl;
    @JsonProperty("creator")
    List<String> creator;

    @JsonProperty("description")
    String description;
    @JsonProperty("title")
    String title;

    @JsonProperty("language")
    String language;

    @JsonProperty("pubDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date pubdate;

    @JsonProperty("country")
    List<String> country;

    @JsonProperty("category")
    List<String> category;


    @Override
    public String toString() {
        return "Result{" +
                "imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", creator=" + creator +
                ", description='" + description + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", country=" + country +
                '}';
    }
}
