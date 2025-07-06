package com.learningplatform.mapper;

import com.learningplatform.dto.ProjectDto;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Project;
import com.learningplatform.entity.User;
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
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDto toDto(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectDto projectDto = new ProjectDto();

        projectDto.setCourseId( projectCourseId( project ) );
        projectDto.setCourseTitre( projectCourseTitre( project ) );
        projectDto.setStudentId( projectStudentId( project ) );
        projectDto.setStudentNom( projectStudentNom( project ) );
        projectDto.setCorrectedById( projectCorrectedById( project ) );
        projectDto.setCorrectedByNom( projectCorrectedByNom( project ) );
        projectDto.setId( project.getId() );
        projectDto.setDescription( project.getDescription() );
        projectDto.setFileUrl( project.getFileUrl() );
        projectDto.setProjectLink( project.getProjectLink() );
        projectDto.setStatus( project.getStatus() );
        projectDto.setNote( project.getNote() );
        projectDto.setCommentaire( project.getCommentaire() );
        projectDto.setSubmittedAt( project.getSubmittedAt() );
        projectDto.setCorrectedAt( project.getCorrectedAt() );

        return projectDto;
    }

    @Override
    public Project toEntity(ProjectDto projectDto) {
        if ( projectDto == null ) {
            return null;
        }

        Project.ProjectBuilder project = Project.builder();

        project.course( projectDtoToCourse( projectDto ) );
        project.student( projectDtoToUser( projectDto ) );
        project.correctedBy( projectDtoToUser1( projectDto ) );
        project.id( projectDto.getId() );
        project.description( projectDto.getDescription() );
        project.fileUrl( projectDto.getFileUrl() );
        project.projectLink( projectDto.getProjectLink() );
        project.status( projectDto.getStatus() );
        project.note( projectDto.getNote() );
        project.commentaire( projectDto.getCommentaire() );
        project.submittedAt( projectDto.getSubmittedAt() );
        project.correctedAt( projectDto.getCorrectedAt() );

        return project.build();
    }

    @Override
    public List<ProjectDto> toDtoList(List<Project> projects) {
        if ( projects == null ) {
            return null;
        }

        List<ProjectDto> list = new ArrayList<ProjectDto>( projects.size() );
        for ( Project project : projects ) {
            list.add( toDto( project ) );
        }

        return list;
    }

    private Long projectCourseId(Project project) {
        if ( project == null ) {
            return null;
        }
        Course course = project.getCourse();
        if ( course == null ) {
            return null;
        }
        Long id = course.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectCourseTitre(Project project) {
        if ( project == null ) {
            return null;
        }
        Course course = project.getCourse();
        if ( course == null ) {
            return null;
        }
        String titre = course.getTitre();
        if ( titre == null ) {
            return null;
        }
        return titre;
    }

    private Long projectStudentId(Project project) {
        if ( project == null ) {
            return null;
        }
        User student = project.getStudent();
        if ( student == null ) {
            return null;
        }
        Long id = student.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectStudentNom(Project project) {
        if ( project == null ) {
            return null;
        }
        User student = project.getStudent();
        if ( student == null ) {
            return null;
        }
        String nom = student.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    private Long projectCorrectedById(Project project) {
        if ( project == null ) {
            return null;
        }
        User correctedBy = project.getCorrectedBy();
        if ( correctedBy == null ) {
            return null;
        }
        Long id = correctedBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectCorrectedByNom(Project project) {
        if ( project == null ) {
            return null;
        }
        User correctedBy = project.getCorrectedBy();
        if ( correctedBy == null ) {
            return null;
        }
        String nom = correctedBy.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    protected Course projectDtoToCourse(ProjectDto projectDto) {
        if ( projectDto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.id( projectDto.getCourseId() );

        return course.build();
    }

    protected User projectDtoToUser(ProjectDto projectDto) {
        if ( projectDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( projectDto.getStudentId() );

        return user.build();
    }

    protected User projectDtoToUser1(ProjectDto projectDto) {
        if ( projectDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( projectDto.getCorrectedById() );

        return user.build();
    }
}
