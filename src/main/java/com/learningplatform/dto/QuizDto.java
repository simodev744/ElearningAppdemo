package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizDto {
    
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotNull(message = "L'ID du cours est obligatoire")
    private Long courseId;
    
    private String courseTitre;
    
    private List<QuestionDto> questions;
    
    private LocalDateTime createdAt;
}