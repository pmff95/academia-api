package com.example.demo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(

        @Schema(description = "CEP", example = "12345-678")
        @NotBlank(message = "O CEP é obrigatório")
        @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 e 9 caracteres")
        String cep,

        @Schema(description = "Endereço", example = "Rua das Flores")
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @Schema(description = "Número", example = "123")
        @NotBlank(message = "O número é obrigatório")
        String numero,

        @Schema(description = "Cidade", example = "São Paulo")
        @NotBlank(message = "A cidade é obrigatória")
        String cidade,

        @Schema(description = "Estado", example = "SP")
        @NotBlank(message = "O estado é obrigatório")
        @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres")
        String estado

) {
}
