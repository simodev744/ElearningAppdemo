package com.learningplatform.mapper;

import com.learningplatform.dto.CourseDto;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.User;
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
public class CourseMapperImpl implements CourseMapper {

    @Autowired
    private CourseContentMapper courseContentMapper;

    @Override
    public CourseDto toDto(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseDto courseDto = new CourseDto();

        courseDto.setFormateurId( courseFormateurId( course ) );
        courseDto.setFormateurNom( courseFormateurNom( course ) );
        courseDto.setId( course.getId() );
        courseDto.setTitre( course.getTitre() );
        courseDto.setCategorie( course.getCategorie() );
        courseDto.setStatut( course.getStatut() );
        courseDto.setEstPayant( course.getEstPayant() );
        courseDto.setCourseContent( courseContentMapper.toDto( course.getCourseContent() ) );
        courseDto.setCreatedAt( course.getCreatedAt() );
        courseDto.setUpdatedAt( course.getUpdatedAt() );

        return courseDto;
    }

    @Override
    public Course toEntity(CourseDto courseDto) {
        if ( courseDto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.formateur( courseDtoToUser( courseDto ) );
        course.id( courseDto.getId() );
        course.titre( courseDto.getTitre() );
        course.categorie( courseDto.getCategorie() );
        course.statut( courseDto.getStatut() );
        course.estPayant( courseDto.getEstPayant() );
        course.courseContent( courseContentMapper.toEntity( courseDto.getCourseContent() ) );
        course.createdAt( courseDto.getCreatedAt() );
        course.updatedAt( courseDto.getUpdatedAt() );

        return course.build();
    }

    @Override
    public List<CourseDto> toDtoList(List<Course> courses) {
        if ( courses == null ) {
            return null;
        }

        List<CourseDto> list = new ArrayList<CourseDto>( courses.size() );
        for ( Course course : courses ) {
            list.add( toDto( course ) );
        }

        return list;
    }

    private Long courseFormateurId(Course course) {
        if ( course == null ) {
            return null;
        }
        User formateur = course.getFormateur();
        if ( formateur == null ) {
            return null;
        }
        Long id = formateur.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String courseFormateurNom(Course course) {
        if ( course == null ) {
            return null;
        }
        User formateur = course.getFormateur();
        if ( formateur == null ) {
            return null;
        }
        String nom = formateur.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected User courseDtoToUser(CourseDto courseDto) {
        if ( courseDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( courseDto.getFormateurId() );

        return user.build();
    }
}
