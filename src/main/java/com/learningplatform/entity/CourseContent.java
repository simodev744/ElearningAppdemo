package com.learningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "course_contents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseContent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String textContent;
    
    @ElementCollection
    @CollectionTable(name = "course_pdf_urls", joinColumns = @JoinColumn(name = "course_content_id"))
    @Column(name = "pdf_url")
    private List<String> pdfUrls;
    
    @ElementCollection
    @CollectionTable(name = "course_video_urls", joinColumns = @JoinColumn(name = "course_content_id"))
    @Column(name = "video_url")
    private List<String> videoUrls;
}