package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDto {
    
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;
    
    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;
    
    private Long authorId;
    private String authorNom;
    
    private List<CommentDto> comments;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}