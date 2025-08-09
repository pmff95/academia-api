package com.example.demo.dto.instituicao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EscolaEnderecoRequest(

        @Schema(description = "Código postal")
        @NotBlank(message = "CEP é obrigatório.")
        @Size(max = 10, message = "CEP deve ter no máximo 10 caracteres.")
        String cep,

        @Schema(description = "Nome da rua")
        @NotBlank(message = "Endereço é obrigatório.")
        @Size(max = 100, message = "Endereço deve ter no máximo 100 caracteres.")
        String endereco,

        @Schema(description = "Número do endereço")
        @NotBlank(message = "Número é obrigatório.")
        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres.")
        String numero,

        @Schema(description = "Bairro")
        @NotBlank(message = "Bairro é obrigatório.")
        @Size(max = 50, message = "Bairro deve ter no máximo 50 caracteres.")
        String bairro,

        @Schema(description = "Complemento do endereço")
        @NotBlank(message = "Complemento é obrigatório.")
        @Size(max = 50, message = "Complemento deve ter no máximo 50 caracteres.")
        String complemento,

        @Schema(description = "Cidade")
        @NotBlank(message = "Cidade é obrigatória.")
        @Size(max = 50, message = "Cidade deve ter no máximo 50 caracteres.")
        String cidade,

        @Schema(description = "UF do estado")
        @NotBlank(message = "Estado é obrigatório.")
        @Size(max = 2, message = "Estado deve ter exatamente 2 caracteres.")
        String estado

) {
}
