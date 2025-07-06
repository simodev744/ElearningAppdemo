package com.learningplatform.dto;

import com.learningplatform.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String token;
    private String email;
    private String nom;
    private Role role;
    private Long userId;
}