package com.learningplatform.mapper;

import com.learningplatform.dto.QuestionDto;
import com.learningplatform.entity.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface QuestionMapper {
    
    QuestionDto toDto(Question question);
    
    Question toEntity(QuestionDto questionDto);
    
    List<QuestionDto> toDtoList(List<Question> questions);
}