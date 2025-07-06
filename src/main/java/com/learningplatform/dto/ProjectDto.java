package com.learningplatform.dto;

import com.learningplatform.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDto {
    
    private Long id;
    
    @NotNull(message = "L'ID du cours est obligatoire")
    private Long courseId;
    
    private String courseTitre;
    
    private Long studentId;
    private String studentNom;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    private String fileUrl;
    private String projectLink;
    
    private ProjectStatus status;
    
    private Integer note;
    private String commentaire;
    
    private Long correctedById;
    private String correctedByNom;
    
    private LocalDateTime submittedAt;
    private LocalDateTime correctedAt;
}