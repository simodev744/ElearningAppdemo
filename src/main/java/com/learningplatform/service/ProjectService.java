package com.learningplatform.service;

import com.learningplatform.dto.ProjectDto;
import com.learningplatform.entity.*;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.mapper.ProjectMapper;
import com.learningplatform.repository.CourseRepository;
import com.learningplatform.repository.ProjectRepository;
import com.learningplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    
    public ProjectDto submitProject(ProjectDto projectDto, Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        
        if (student.getRole() != Role.ETUDIANT) {
            throw new UnauthorizedException("Seuls les étudiants peuvent soumettre des projets");
        }
        
        Course course = courseRepository.findById(projectDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        

        if (projectRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("Vous avez déjà soumis un projet pour ce cours");
        }
        
        Project project = projectMapper.toEntity(projectDto);
        project.setStudent(student);
        project.setCourse(course);
        project.setStatus(ProjectStatus.SOUMIS);
        
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }
    
    public ProjectDto correctProject(Long projectId, Integer note, String commentaire, Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        if (formateur.getRole() != Role.FORMATEUR) {
            throw new UnauthorizedException("Seuls les formateurs peuvent corriger des projets");
        }
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé"));
        
        // Vérifier que le formateur est bien celui du cours
        if (!project.getCourse().getFormateur().getId().equals(formateur.getId())) {
            throw new UnauthorizedException("Vous ne pouvez corriger que les projets de vos propres cours");
        }
        
        project.setNote(note);
        project.setCommentaire(commentaire);
        project.setStatus(ProjectStatus.CORRIGE);
        project.setCorrectedBy(formateur);
        project.setCorrectedAt(LocalDateTime.now());
        
        Project correctedProject = projectRepository.save(project);
        return projectMapper.toDto(correctedProject);
    }
    
    public List<ProjectDto> getProjectsByStudent(Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        List<Project> projects = projectRepository.findByStudent(student);
        return projectMapper.toDtoList(projects);
    }
    
    public List<ProjectDto> getProjectsByCourse(Long courseId, Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        
        if (formateur.getRole() != Role.FORMATEUR || !course.getFormateur().getId().equals(formateur.getId())) {
            throw new UnauthorizedException("Vous ne pouvez voir que les projets de vos propres cours");
        }
        
        List<Project> projects = projectRepository.findByCourse(course);
        return projectMapper.toDtoList(projects);
    }
    
    public List<ProjectDto> getPendingProjects(Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        if (formateur.getRole() != Role.FORMATEUR) {
            throw new UnauthorizedException("Seuls les formateurs peuvent voir les projets en attente");
        }
        
        List<Project> projects = projectRepository.findByStatus(ProjectStatus.SOUMIS);
        // Filtrer pour ne montrer que les projets des cours du formateur
        return projects.stream()
                .filter(project -> project.getCourse().getFormateur().getId().equals(formateur.getId()))
                .map(projectMapper::toDto)
                .toList();
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}