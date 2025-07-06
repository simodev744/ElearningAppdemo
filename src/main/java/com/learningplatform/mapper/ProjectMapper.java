package com.learningplatform.mapper;

import com.learningplatform.dto.ProjectDto;
import com.learningplatform.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.titre", target = "courseTitre")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.nom", target = "studentNom")
    @Mapping(source = "correctedBy.id", target = "correctedById")
    @Mapping(source = "correctedBy.nom", target = "correctedByNom")
    ProjectDto toDto(Project project);
    
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "correctedById", target = "correctedBy.id")
    Project toEntity(ProjectDto projectDto);
    
    List<ProjectDto> toDtoList(List<Project> projects);
}