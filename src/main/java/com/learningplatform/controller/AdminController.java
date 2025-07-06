package com.learningplatform.controller;

import com.learningplatform.dto.DashboardStatsDto;
import com.learningplatform.entity.User;
import com.learningplatform.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administration", description = "Fonctionnalités d'administration")
public class AdminController {
    
    private final AdminService adminService;
    
    @GetMapping("/dashboard")
    @Operation(summary = "Récupérer les statistiques du tableau de bord")
    public ResponseEntity<DashboardStatsDto> getDashboardStats(Authentication authentication) {
        return ResponseEntity.ok(adminService.getDashboardStats(authentication));
    }
    
    @GetMapping("/users")
    @Operation(summary = "Récupérer tous les utilisateurs")
    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) {
        return ResponseEntity.ok(adminService.getAllUsers(authentication));
    }
    
    @PatchMapping("/users/{id}/activate")
    @Operation(summary = "Activer un utilisateur")
    public ResponseEntity<User> activateUser(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(adminService.activateUser(id, authentication));
    }
    
    @PatchMapping("/users/{id}/deactivate")
    @Operation(summary = "Désactiver un utilisateur")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(adminService.deactivateUser(id, authentication));
    }
    
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        adminService.deleteUser(id, authentication);
        return ResponseEntity.noContent().build();
    }
}