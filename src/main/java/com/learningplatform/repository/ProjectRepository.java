package com.learningplatform.repository;

import com.learningplatform.entity.Course;
import com.learningplatform.entity.Project;
import com.learningplatform.entity.ProjectStatus;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByStudent(User student);
    
    List<Project> findByCourse(Course course);
    
    List<Project> findByStatus(ProjectStatus status);
    
    Optional<Project> findByStudentAndCourse(User student, Course course);
    
    boolean existsByStudentAndCourse(User student, Course course);
}