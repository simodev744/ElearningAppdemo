package com.learningplatform.service;

import com.learningplatform.dto.CourseDto;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.CourseStatus;
import com.learningplatform.entity.Role;
import com.learningplatform.entity.User;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.mapper.CourseMapper;
import com.learningplatform.repository.CourseRepository;
import com.learningplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    
    public List<CourseDto> getAllValidatedCourses() {
        List<Course> courses = courseRepository.findByStatut(CourseStatus.VALIDE);
        return courseMapper.toDtoList(courses);
    }
    
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDtoList(courses);
    }
    
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        return courseMapper.toDto(course);
    }
    
    public CourseDto createCourse(CourseDto courseDto, Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        if (formateur.getRole() != Role.FORMATEUR) {
            throw new UnauthorizedException("Seuls les formateurs peuvent créer des cours");
        }
        
        Course course = courseMapper.toEntity(courseDto);
        course.setFormateur(formateur);
        course.setStatut(CourseStatus.EN_ATTENTE);
        
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }
    
    public CourseDto updateCourse(Long id, CourseDto courseDto, Authentication authentication) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        
        User user = getUserFromAuthentication(authentication);
        
        if (user.getRole() != Role.ADMIN && !existingCourse.getFormateur().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier ce cours");
        }
        
        existingCourse.setTitre(courseDto.getTitre());
        existingCourse.setCategorie(courseDto.getCategorie());
        existingCourse.setEstPayant(courseDto.getEstPayant());
        
        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }
    
    public CourseDto validateCourse(Long id, Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent valider les cours");
        }
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        
        course.setStatut(CourseStatus.VALIDE);
        Course validatedCourse = courseRepository.save(course);
        
        return courseMapper.toDto(validatedCourse);
    }
    
    public CourseDto rejectCourse(Long id, Authentication authentication) {
        User admin = getUserFromAuthentication(authentication);
        
        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seuls les administrateurs peuvent rejeter les cours");
        }
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        
        course.setStatut(CourseStatus.REJETE);
        Course rejectedCourse = courseRepository.save(course);
        
        return courseMapper.toDto(rejectedCourse);
    }
    
    public List<CourseDto> getCoursesByFormateur(Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        List<Course> courses = courseRepository.findByFormateur(formateur);
        return courseMapper.toDtoList(courses);
    }
    
    public List<CourseDto> getPendingCourses() {
        List<Course> courses = courseRepository.findByStatut(CourseStatus.EN_ATTENTE);
        return courseMapper.toDtoList(courses);
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}