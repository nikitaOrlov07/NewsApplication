package com.example.demo.DTO;

import com.example.demo.models.News;
import lombok.Data;

import java.util.List;
@Data
public class NewsPagination {
    private List<News> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

