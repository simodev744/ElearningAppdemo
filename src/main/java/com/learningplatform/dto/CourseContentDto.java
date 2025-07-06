package com.learningplatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseContentDto {
    
    private Long id;
    private String textContent;
    private List<String> pdfUrls;
    private List<String> videoUrls;
}