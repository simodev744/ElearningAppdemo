    package com.learningplatform.dto;

    import jakarta.validation.constraints.NotBlank;
    import lombok.Data;

    @Data
    public class AnswerDto {

        private Long id;

        @NotBlank(message = "Le texte de la réponse est obligatoire")
        private String answerText;
    }