package com.learningplatform.mapper;

import com.learningplatform.dto.CourseContentDto;
import com.learningplatform.entity.CourseContent;
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
public class CourseContentMapperImpl implements CourseContentMapper {

    @Override
    public CourseContentDto toDto(CourseContent courseContent) {
        if ( courseContent == null ) {
            return null;
        }

        CourseContentDto courseContentDto = new CourseContentDto();

        courseContentDto.setId( courseContent.getId() );
        courseContentDto.setTextContent( courseContent.getTextContent() );
        List<String> list = courseContent.getPdfUrls();
        if ( list != null ) {
            courseContentDto.setPdfUrls( new ArrayList<String>( list ) );
        }
        List<String> list1 = courseContent.getVideoUrls();
        if ( list1 != null ) {
            courseContentDto.setVideoUrls( new ArrayList<String>( list1 ) );
        }

        return courseContentDto;
    }

    @Override
    public CourseContent toEntity(CourseContentDto courseContentDto) {
        if ( courseContentDto == null ) {
            return null;
        }

        CourseContent.CourseContentBuilder courseContent = CourseContent.builder();

        courseContent.id( courseContentDto.getId() );
        courseContent.textContent( courseContentDto.getTextContent() );
        List<String> list = courseContentDto.getPdfUrls();
        if ( list != null ) {
            courseContent.pdfUrls( new ArrayList<String>( list ) );
        }
        List<String> list1 = courseContentDto.getVideoUrls();
        if ( list1 != null ) {
            courseContent.videoUrls( new ArrayList<String>( list1 ) );
        }

        return courseContent.build();
    }
}
