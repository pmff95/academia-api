package com.example.demo.dto.instituicao;

import com.example.demo.common.validation.annotation.CNPJ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EscolaRequest(
        @Schema(description = "Nome da escola", example = "Nome da Escola")
        @NotBlank(message = "Nome deve ser preenchido")
        @Size(min = 5, message = "Nome deve ter pelo menos 5 caracteres")
        String nome,

        @Schema(description = "Nome curto da escola", example = "Escola")
        @NotBlank(message = "Nome curto deve ser preenchido")
        @Size(min = 2, message = "Nome curto deve ter pelo menos 2 caracteres")
        String nomeCurto,
        @Schema(description = "CNPJ da escola", example = "00.000.000/0001-00")
        @NotBlank(message = "CNPJ deve ser preenchido")
        @CNPJ String cnpj,

        @Schema(description = "Dados de endereço da escola")
        EscolaEnderecoRequest endereco
) {

}
