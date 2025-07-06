package com.learningplatform.controller;

import com.learningplatform.dto.ArticleDto;
import com.learningplatform.dto.CommentDto;
import com.learningplatform.service.ArticleService;
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
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Gestion des articles de blog")
public class ArticleController {
    
    private final ArticleService articleService;
    
    @GetMapping
    @Operation(summary = "Récupérer tous les articles")
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un article par ID")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FORMATEUR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Créer un nouvel article")
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleDto articleDto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(articleDto, authentication));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FORMATEUR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Modifier un article")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDto articleDto, Authentication authentication) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDto, authentication));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FORMATEUR') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Supprimer un article")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id, Authentication authentication) {
        articleService.deleteArticle(id, authentication);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Récupérer les articles par catégorie")
    public ResponseEntity<List<ArticleDto>> getArticlesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(articleService.getArticlesByCategory(category));
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Récupérer toutes les catégories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(articleService.getAllCategories());
    }
    
    @PostMapping("/{id}/comments")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ajouter un commentaire à un article")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long id, @Valid @RequestBody CommentDto commentDto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.addComment(id, commentDto, authentication));
    }
    
    @GetMapping("/{id}/comments")
    @Operation(summary = "Récupérer les commentaires d'un article")
    public ResponseEntity<List<CommentDto>> getCommentsByArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getCommentsByArticle(id));
    }
}