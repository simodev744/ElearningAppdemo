package com.learningplatform.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.learningplatform.entity.*;
import com.learningplatform.exception.ResourceNotFoundException;
import com.learningplatform.exception.UnauthorizedException;
import com.learningplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateService {
    
    private final CertificateRepository certificateRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final ProjectRepository projectRepository;
    
    private static final String CERTIFICATES_DIR = "certificates/";
    private static final int MIN_QUIZ_SCORE_PERCENTAGE = 70;
    private static final int MIN_PROJECT_SCORE = 12; // Sur 20
    
    public Certificate generateCertificate(Long courseId, Authentication authentication) throws IOException {
        User student = getUserFromAuthentication(authentication);
        
        if (student.getRole() != Role.ETUDIANT) {
            throw new UnauthorizedException("Seuls les étudiants peuvent générer des certificats");
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        
        // Vérifier si le certificat existe déjà
        if (certificateRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalStateException("Un certificat a déjà été généré pour ce cours");
        }
        
        // Vérifier les conditions de réussite
        if (!hasPassedQuiz(student, course) || !hasPassedProject(student, course)) {
            throw new IllegalStateException("Vous devez réussir le quiz et le projet pour obtenir le certificat");
        }
        
        // Générer le certificat PDF
        String certificateNumber = generateCertificateNumber();
        String pdfPath = generateCertificatePdf(student, course, certificateNumber);
        
        Certificate certificate = Certificate.builder()
                .student(student)
                .course(course)
                .certificateNumber(certificateNumber)
                .pdfPath(pdfPath)
                .build();
        
        return certificateRepository.save(certificate);
    }
    
    public List<Certificate> getCertificatesByStudent(Authentication authentication) {
        User student = getUserFromAuthentication(authentication);
        return certificateRepository.findByStudent(student);
    }
    
    public byte[] downloadCertificate(String certificateNumber, Authentication authentication) throws IOException {
        User user = getUserFromAuthentication(authentication);
        
        Certificate certificate = certificateRepository.findByCertificateNumber(certificateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Certificat non trouvé"));
        
        // Vérifier que l'utilisateur peut télécharger ce certificat
        if (!certificate.getStudent().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à télécharger ce certificat");
        }
        
        Path pdfPath = Paths.get(certificate.getPdfPath());
        return Files.readAllBytes(pdfPath);
    }
    
    private boolean hasPassedQuiz(User student, Course course) {
        return quizResultRepository.findByUser(student).stream()
                .filter(result -> result.getQuiz().getCourse().getId().equals(course.getId()))
                .anyMatch(result -> {
                    double percentage = (double) result.getScore() / result.getTotalQuestions() * 100;
                    return percentage >= MIN_QUIZ_SCORE_PERCENTAGE;
                });
    }
    
    private boolean hasPassedProject(User student, Course course) {
        return projectRepository.findByStudentAndCourse(student, course)
                .map(project -> project.getNote() != null && project.getNote() >= MIN_PROJECT_SCORE)
                .orElse(false);
    }
    
    private String generateCertificateNumber() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateCertificatePdf(User student, Course course, String certificateNumber) throws IOException {
        // Créer le répertoire s'il n'existe pas
        Path certificatesDir = Paths.get(CERTIFICATES_DIR);
        if (!Files.exists(certificatesDir)) {
            Files.createDirectories(certificatesDir);
        }
        
        String fileName = certificateNumber + ".pdf";
        String filePath = CERTIFICATES_DIR + fileName;
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Titre du certificat
            document.add(new Paragraph("CERTIFICAT DE RÉUSSITE")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold());
            
            document.add(new Paragraph("\n"));
            
            // Contenu du certificat
            document.add(new Paragraph("Nous certifions par la présente que")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));
            
            document.add(new Paragraph(student.getNom())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());
            
            document.add(new Paragraph("a suivi avec succès le cours")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));
            
            document.add(new Paragraph(course.getTitre())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold());
            
            document.add(new Paragraph("dispensé par " + course.getFormateur().getNom())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));
            
            document.add(new Paragraph("\n"));
            
            // Informations du certificat
            document.add(new Paragraph("Numéro de certificat: " + certificateNumber)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            
            document.add(new Paragraph("Date de délivrance: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            
            document.add(new Paragraph("Organisation: Plateforme de Formation en Ligne")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            
            document.close();
            
            // Sauvegarder le PDF
            Files.write(Paths.get(filePath), baos.toByteArray());
        }
        
        return filePath;
    }
    
    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }
}