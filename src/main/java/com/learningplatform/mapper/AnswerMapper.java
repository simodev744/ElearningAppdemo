package com.learningplatform.mapper;

import com.learningplatform.dto.AnswerDto;
import com.learningplatform.entity.Answer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    
    AnswerDto toDto(Answer answer);
    
    Answer toEntity(AnswerDto answerDto);
    
    List<AnswerDto> toDtoList(List<Answer> answers);
}