package com.learningplatform.repository;

import com.learningplatform.entity.Certificate;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    
    List<Certificate> findByStudent(User student);
    
    List<Certificate> findByCourse(Course course);
    
    Optional<Certificate> findByStudentAndCourse(User student, Course course);
    
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    
    boolean existsByStudentAndCourse(User student, Course course);
}