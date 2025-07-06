package com.learningplatform.controller;

import com.learningplatform.entity.Certificate;
import com.learningplatform.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Certificates", description = "Gestion des certificats")
public class CertificateController {
    
    private final CertificateService certificateService;
    
    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Générer un certificat pour un cours")
    public ResponseEntity<Certificate> generateCertificate(@PathVariable Long courseId, Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificateService.generateCertificate(courseId, authentication));
    }
    
    @GetMapping("/my-certificates")
    @PreAuthorize("hasRole('ETUDIANT')")
    @Operation(summary = "Récupérer mes certificats")
    public ResponseEntity<List<Certificate>> getMyCertificates(Authentication authentication) {
        return ResponseEntity.ok(certificateService.getCertificatesByStudent(authentication));
    }
    
    @GetMapping("/download/{certificateNumber}")
    @Operation(summary = "Télécharger un certificat")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String certificateNumber, Authentication authentication) throws IOException {
        byte[] pdfContent = certificateService.downloadCertificate(certificateNumber, authentication);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", certificateNumber + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}