package com.learningplatform.controller;

import com.learningplatform.dto.ProjectDto;
import com.learningplatform.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Projects", description = "Gestion des projets")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @PostMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Soumettre un projet")
    public ResponseEntity<ProjectDto> submitProject(@Valid @RequestBody ProjectDto projectDto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.submitProject(projectDto, authentication));
    }
    
    @PatchMapping("/{id}/correct")
    @PreAuthorize("hasRole('FORMATEUR')")
    @Operation(summary = "Corriger un projet")
    public ResponseEntity<ProjectDto> correctProject(
            @PathVariable Long id,
            @RequestParam Integer note,
            @RequestParam String commentaire,
            Authentication authentication) {
        return ResponseEntity.ok(projectService.correctProject(id, note, commentaire, authentication));
    }
    
    @GetMapping("/my-projects")
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Récupérer mes projets")
    public ResponseEntity<List<ProjectDto>> getMyProjects(Authentication authentication) {
        return ResponseEntity.ok(projectService.getProjectsByStudent(authentication));
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('FORMATEUR')")
    @Operation(summary = "Récupérer les projets d'un cours")
    public ResponseEntity<List<ProjectDto>> getProjectsByCourse(@PathVariable Long courseId, Authentication authentication) {
        return ResponseEntity.ok(projectService.getProjectsByCourse(courseId, authentication));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('FORMATEUR')")
    @Operation(summary = "Récupérer les projets en attente de correction")
    public ResponseEntity<List<ProjectDto>> getPendingProjects(Authentication authentication) {
        return ResponseEntity.ok(projectService.getPendingProjects(authentication));
    }
}