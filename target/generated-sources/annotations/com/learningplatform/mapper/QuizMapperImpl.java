package com.learningplatform.mapper;

import com.learningplatform.dto.QuestionDto;
import com.learningplatform.dto.QuizDto;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Question;
import com.learningplatform.entity.Quiz;
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
public class QuizMapperImpl implements QuizMapper {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public QuizDto toDto(Quiz quiz) {
        if ( quiz == null ) {
            return null;
        }

        QuizDto quizDto = new QuizDto();

        quizDto.setCourseId( quizCourseId( quiz ) );
        quizDto.setCourseTitre( quizCourseTitre( quiz ) );
        quizDto.setId( quiz.getId() );
        quizDto.setTitre( quiz.getTitre() );
        quizDto.setQuestions( questionMapper.toDtoList( quiz.getQuestions() ) );
        quizDto.setCreatedAt( quiz.getCreatedAt() );

        return quizDto;
    }

    @Override
    public Quiz toEntity(QuizDto quizDto) {
        if ( quizDto == null ) {
            return null;
        }

        Quiz.QuizBuilder quiz = Quiz.builder();

        quiz.course( quizDtoToCourse( quizDto ) );
        quiz.id( quizDto.getId() );
        quiz.titre( quizDto.getTitre() );
        quiz.questions( questionDtoListToQuestionList( quizDto.getQuestions() ) );
        quiz.createdAt( quizDto.getCreatedAt() );

        return quiz.build();
    }

    @Override
    public List<QuizDto> toDtoList(List<Quiz> quizzes) {
        if ( quizzes == null ) {
            return null;
        }

        List<QuizDto> list = new ArrayList<QuizDto>( quizzes.size() );
        for ( Quiz quiz : quizzes ) {
            list.add( toDto( quiz ) );
        }

        return list;
    }

    private Long quizCourseId(Quiz quiz) {
        if ( quiz == null ) {
            return null;
        }
        Course course = quiz.getCourse();
        if ( course == null ) {
            return null;
        }
        Long id = course.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String quizCourseTitre(Quiz quiz) {
        if ( quiz == null ) {
            return null;
        }
        Course course = quiz.getCourse();
        if ( course == null ) {
            return null;
        }
        String titre = course.getTitre();
        if ( titre == null ) {
            return null;
        }
        return titre;
    }

    protected Course quizDtoToCourse(QuizDto quizDto) {
        if ( quizDto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.id( quizDto.getCourseId() );

        return course.build();
    }

    protected List<Question> questionDtoListToQuestionList(List<QuestionDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Question> list1 = new ArrayList<Question>( list.size() );
        for ( QuestionDto questionDto : list ) {
            list1.add( questionMapper.toEntity( questionDto ) );
        }

        return list1;
    }
}
