package com.learningplatform.mapper;

import com.learningplatform.dto.AnswerDto;
import com.learningplatform.dto.QuestionDto;
import com.learningplatform.entity.Answer;
import com.learningplatform.entity.Question;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T17:15:49+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public QuestionDto toDto(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionDto questionDto = new QuestionDto();

        questionDto.setId( question.getId() );
        questionDto.setQuestionText( question.getQuestionText() );
        questionDto.setAnswers( answerMapper.toDtoList( question.getAnswers() ) );
        questionDto.setCorrectAnswerIndex( question.getCorrectAnswerIndex() );

        return questionDto;
    }

    @Override
    public Question toEntity(QuestionDto questionDto) {
        if ( questionDto == null ) {
            return null;
        }

        Question.QuestionBuilder question = Question.builder();

        question.id( questionDto.getId() );
        question.questionText( questionDto.getQuestionText() );
        question.answers( answerDtoListToAnswerList( questionDto.getAnswers() ) );
        question.correctAnswerIndex( questionDto.getCorrectAnswerIndex() );

        return question.build();
    }

    @Override
    public List<QuestionDto> toDtoList(List<Question> questions) {
        if ( questions == null ) {
            return null;
        }

        List<QuestionDto> list = new ArrayList<QuestionDto>( questions.size() );
        for ( Question question : questions ) {
            list.add( toDto( question ) );
        }

        return list;
    }

    protected List<Answer> answerDtoListToAnswerList(List<AnswerDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Answer> list1 = new ArrayList<Answer>( list.size() );
        for ( AnswerDto answerDto : list ) {
            list1.add( answerMapper.toEntity( answerDto ) );
        }

        return list1;
    }
}
