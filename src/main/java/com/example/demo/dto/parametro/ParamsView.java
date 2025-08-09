package com.example.demo.dto.parametro;

import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.enums.TipoValor;

public record ParamsView(
        String chave,
        String descricao,

        TipoParametro tipo,
        TipoValor tipoValor,
        String valorDefault,
        String valorCustomizado,

        Boolean necessarioConfirmacao,
        String mensagemConfirmacaoAtivo,
        String mensagemConfirmacaoInativo,
        String mensagemConfirmacaoAlteracao) {
}
