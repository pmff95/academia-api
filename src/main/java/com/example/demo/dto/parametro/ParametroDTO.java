package com.example.demo.dto.parametro;

import com.example.demo.common.validation.annotation.Chave;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.enums.TipoValor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ParametroDTO(
        @Schema(description = "Chave única do parâmetro", example = "limite_maximo")
        @NotBlank(message = "Chave é obrigatória")
        @Chave
        String chave,

        @Schema(description = "Descrição do parâmetro", example = "Limite máximo de alunos")
        String descricao,

        @Schema(description = "Valor padrão do parâmetro", example = "100")
        String valorDefault,

        @Schema(description = "Tipo do valor")
        TipoValor tipoValor,

        @Schema(description = "Indica se exige confirmação")
        Boolean necessarioConfirmacao,

        @Schema(description = "Mensagem ao ativar")
        String mensagemConfirmacaoAtivo,

        @Schema(description = "Mensagem ao desativar")
        String mensagemConfirmacaoInativo,

        @Schema(description = "Mensagem ao alterar")
        String mensagemConfirmacaoAlteracao,

        @Schema(description = "Tipo do parâmetro")
        TipoParametro tipo
) {
}
