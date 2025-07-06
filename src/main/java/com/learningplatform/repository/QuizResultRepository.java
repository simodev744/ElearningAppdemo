package com.learningplatform.repository;

import com.learningplatform.entity.Quiz;
import com.learningplatform.entity.QuizResult;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    
    List<QuizResult> findByUser(User user);
    
    List<QuizResult> findByQuiz(Quiz quiz);
    
    Optional<QuizResult> findByUserAndQuiz(User user, Quiz quiz);
    
    boolean existsByUserAndQuiz(User user, Quiz quiz);
}