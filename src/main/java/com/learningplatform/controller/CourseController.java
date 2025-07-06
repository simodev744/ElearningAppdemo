package com.learningplatform.controller;

import com.learningplatform.dto.CourseDto;
import com.learningplatform.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Gestion des cours")
public class CourseController {
    
    private final CourseService courseService;
    
    @GetMapping
    @Operation(summary = "Récupérer tous les cours validés")
    public ResponseEntity<List<CourseDto>> getAllValidatedCourses() {
        return ResponseEntity.ok(courseService.getAllValidatedCourses());
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Récupérer tous les cours (Admin uniquement)")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un cours par ID")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FORMATEUR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Créer un nouveau cours")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(courseDto, authentication));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FORMATEUR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Modifier un cours")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto, Authentication authentication) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto, authentication));
    }
    
    @PatchMapping("/{id}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Valider un cours")
    public ResponseEntity<CourseDto> validateCourse(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(courseService.validateCourse(id, authentication));
    }
    
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Rejeter un cours")
    public ResponseEntity<CourseDto> rejectCourse(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(courseService.rejectCourse(id, authentication));
    }
    
    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('FORMATEUR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Récupérer mes cours")
    public ResponseEntity<List<CourseDto>> getMyCourses(Authentication authentication) {
        return ResponseEntity.ok(courseService.getCoursesByFormateur(authentication));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Récupérer les cours en attente de validation")
    public ResponseEntity<List<CourseDto>> getPendingCourses() {
        return ResponseEntity.ok(courseService.getPendingCourses());
    }
}