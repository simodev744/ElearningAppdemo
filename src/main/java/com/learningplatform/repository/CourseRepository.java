package com.learningplatform.repository;

import com.learningplatform.entity.Course;
import com.learningplatform.entity.CourseStatus;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByStatut(CourseStatus statut);
    
    List<Course> findByFormateur(User formateur);
    
    List<Course> findByCategorie(String categorie);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.statut = :statut")
    long countByStatut(CourseStatus statut);
    
    @Query("SELECT DISTINCT c.categorie FROM Course c WHERE c.statut = 'VALIDE'")
    List<String> findDistinctCategories();
}