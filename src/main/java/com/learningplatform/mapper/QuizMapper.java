package com.learningplatform.mapper;

import com.learningplatform.dto.QuizDto;
import com.learningplatform.entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuizMapper {
    
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.titre", target = "courseTitre")
    QuizDto toDto(Quiz quiz);
    
    @Mapping(source = "courseId", target = "course.id")
    Quiz toEntity(QuizDto quizDto);
    
    List<QuizDto> toDtoList(List<Quiz> quizzes);
}