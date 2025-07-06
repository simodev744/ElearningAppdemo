package com.learningplatform.service;

import com.learningplatform.dto.QuizDto;
import com.learningplatform.entity.*;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.mapper.QuizMapper;
import com.learningplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    
    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizMapper quizMapper;
    
    public QuizDto createQuiz(QuizDto quizDto, Authentication authentication) {
        User formateur = getUserFromAuthentication(authentication);
        
        if (formateur.getRole() != Role.FORMATEUR) {
            throw new UnauthorizedException("Seuls les formateurs peuvent créer des quiz");
        }
        
        Course course = courseRepository.findById(quizDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        
        if (!course.getFormateur().getId().equals(formateur.getId())) {
            throw new UnauthorizedException("Vous ne pouvez créer des quiz que pour vos propres cours");
        }
        
        Quiz quiz = quizMapper.toEntity(quizDto);
        quiz.setCourse(course);
        
        // Associer les questions au quiz
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(question -> {
                question.setQuiz(quiz);
                if (question.getAnswers() != null) {
                    question.getAnswers().forEach(answer -> answer.setQuestion(question));
                }
            });
        }
        
        Quiz savedQuiz = quizRepository.save(quiz);
        return quizMapper.toDto(savedQuiz);
    }
    
    public List<QuizDto> getQuizzesByCourse(Long courseId) {
        List<Quiz> quizzes = quizRepository.findByCourseId(courseId);
        return quizMapper.toDtoList(quizzes);
    }
    
    public QuizDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé avec l'ID: " + id));
        return quizMapper.toDto(quiz);
    }
    
    public QuizResult submitQuizAnswers(Long quizId, List<Integer> answers, Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        
        if (student.getRole() != Role.ETUDIANT) {
            throw new UnauthorizedException("Seuls les étudiants peuvent passer des quiz");
        }
        
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz non trouvé"));
        
        // Vérifier si l'étudiant a déjà passé ce quiz
        if (quizResultRepository.existsByUserAndQuiz(student, quiz)) {
            throw new IllegalStateException("Vous avez déjà passé ce quiz");
        }
        
        // Calculer le score
        int score = 0;
        List<Question> questions = quiz.getQuestions();
        
        for (int i = 0; i < Math.min(answers.size(), questions.size()); i++) {
            if (answers.get(i).equals(questions.get(i).getCorrectAnswerIndex())) {
                score++;
            }
        }
        
        QuizResult result = QuizResult.builder()
                .user(student)
                .quiz(quiz)
                .score(score)
                .totalQuestions(questions.size())
                .build();
        
        return quizResultRepository.save(result);
    }
    
    public List<QuizResult> getQuizResultsByStudent(Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        return quizResultRepository.findByUser(student);
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}