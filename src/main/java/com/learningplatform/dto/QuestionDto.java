package com.learningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    
    private Long id;
    
    @NotBlank(message = "Le texte de la question est obligatoire")
    private String questionText;
    
    private List<AnswerDto> answers;
    
    @NotNull(message = "L'index de la réponse correcte est obligatoire")
    private Integer correctAnswerIndex;
}