package com.learningplatform.controller;

import com.learningplatform.dto.QuizDto;
import com.learningplatform.entity.QuizResult;
import com.learningplatform.service.QuizService;
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
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Quizzes", description = "Gestion des quiz")
public class QuizController {
    
    private final QuizService quizService;
    
    @PostMapping
    @PreAuthorize("hasRole('FORMATEUR')")
    @Operation(summary = "Créer un nouveau quiz")
    public ResponseEntity<QuizDto> createQuiz(@Valid @RequestBody QuizDto quizDto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(quizService.createQuiz(quizDto, authentication));
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Récupérer les quiz d'un cours")
    public ResponseEntity<List<QuizDto>> getQuizzesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getQuizzesByCourse(courseId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un quiz par ID")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }
    
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Soumettre les réponses d'un quiz")
    public ResponseEntity<QuizResult> submitQuiz(@PathVariable Long id, @RequestBody List<Integer> answers, Authentication authentication) {
        return ResponseEntity.ok(quizService.submitQuizAnswers(id, answers, authentication));
    }
    
    @GetMapping("/my-results")
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Récupérer mes résultats de quiz")
    public ResponseEntity<List<QuizResult>> getMyQuizResults(Authentication authentication) {
        return ResponseEntity.ok(quizService.getQuizResultsByStudent(authentication));
    }
}