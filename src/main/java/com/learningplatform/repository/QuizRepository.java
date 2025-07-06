package com.learningplatform.repository;

import com.learningplatform.entity.Course;
import com.learningplatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    List<Quiz> findByCourse(Course course);
    
    List<Quiz> findByCourseId(Long courseId);
}