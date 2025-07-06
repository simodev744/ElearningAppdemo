package com.learningplatform.mapper;

import com.learningplatform.dto.CourseDto;
import com.learningplatform.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseContentMapper.class})
public interface CourseMapper {
    
    @Mapping(source = "formateur.id", target = "formateurId")
    @Mapping(source = "formateur.nom", target = "formateurNom")
    CourseDto toDto(Course course);
    
    @Mapping(source = "formateurId", target = "formateur.id")
    Course toEntity(CourseDto courseDto);
    
    List<CourseDto> toDtoList(List<Course> courses);
}