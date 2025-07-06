package com.learningplatform.mapper;

import com.learningplatform.dto.CourseContentDto;
import com.learningplatform.entity.CourseContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseContentMapper {
    
    CourseContentDto toDto(CourseContent courseContent);
    
    CourseContent toEntity(CourseContentDto courseContentDto);
}