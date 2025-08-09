package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DisciplinaRequest(

        @Schema(description = "Nome da disciplina", example = "Matemática")
        @NotBlank(message = "O nome da disciplina é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

//        @Schema(description = "Nível de ensino da disciplina", example = "FUNDAMENTAL")
//        @NotBlank(message = "O nível de ensino é obrigatório")
//        NivelEnsino nivelEnsino,

        @Schema(description = "Indica se a disciplina é de itinerário", example = "false")
        boolean itinerario,

        @Schema(description = "Carga horária da disciplina", example = "80")
        @NotNull(message = "Carga horária é obrigatória")
        Integer cargaHoraria

) {
}
