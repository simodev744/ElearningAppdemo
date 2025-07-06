package com.learningplatform.dto;

import com.learningplatform.entity.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDto {
    
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;
    
    private CourseStatus statut;
    
    @NotNull(message = "Le statut payant est obligatoire")
    private Boolean estPayant;
    
    private Long formateurId;
    private String formateurNom;
    
    private CourseContentDto courseContent;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}