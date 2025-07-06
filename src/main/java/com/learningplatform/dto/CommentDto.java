package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    
    private Long id;
    
    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;
    
    private Long articleId;
    
    private Long authorId;
    private String authorNom;
    
    private LocalDateTime createdAt;
}