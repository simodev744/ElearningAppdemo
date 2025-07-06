package com.learningplatform.mapper;

import com.learningplatform.dto.AnswerDto;
import com.learningplatform.entity.Answer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T17:15:49+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public AnswerDto toDto(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerDto answerDto = new AnswerDto();

        answerDto.setId( answer.getId() );
        answerDto.setAnswerText( answer.getAnswerText() );

        return answerDto;
    }

    @Override
    public Answer toEntity(AnswerDto answerDto) {
        if ( answerDto == null ) {
            return null;
        }

        Answer.AnswerBuilder answer = Answer.builder();

        answer.id( answerDto.getId() );
        answer.answerText( answerDto.getAnswerText() );

        return answer.build();
    }

    @Override
    public List<AnswerDto> toDtoList(List<Answer> answers) {
        if ( answers == null ) {
            return null;
        }

        List<AnswerDto> list = new ArrayList<AnswerDto>( answers.size() );
        for ( Answer answer : answers ) {
            list.add( toDto( answer ) );
        }

        return list;
    }
}
