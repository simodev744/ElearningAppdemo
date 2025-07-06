package com.learningplatform.service;

import com.learningplatform.dto.DashboardStatsDto;
import com.learningplatform.entity.CourseStatus;
import com.learningplatform.entity.Role;
import com.learningplatform.entity.User;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ArticleRepository articleRepository;
    
    public DashboardStatsDto getDashboardStats(Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent accéder aux statistiques");
        }
        
        return DashboardStatsDto.builder()
                .totalStudents(userRepository.countByRole(Role.ETUDIANT))
                .totalFormateurs(userRepository.countByRole(Role.FORMATEUR))
                .totalCourses(courseRepository.count())
                .validatedCourses(courseRepository.countByStatut(CourseStatus.VALIDE))
                .pendingCourses(courseRepository.countByStatut(CourseStatus.EN_ATTENTE))
                .totalArticles(articleRepository.countAllArticles())
                .activeUsers(userRepository.countActiveUsers())
                .build();
    }
    
    public List<User> getAllUsers(Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent voir tous les utilisateurs");
        }
        
        return userRepository.findAll();
    }
    
    public User activateUser(Long userId, Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent activer des utilisateurs");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
        
        user.setActif(true);
        return userRepository.save(user);
    }
    
    public User deactivateUser(Long userId, Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent désactiver des utilisateurs");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
        
        user.setActif(false);
        return userRepository.save(user);
    }
    
    public void deleteUser(Long userId, Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent supprimer des utilisateurs");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
        
        userRepository.delete(user);
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}