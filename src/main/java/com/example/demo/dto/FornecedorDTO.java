package com.example.demo.dto;

import com.example.demo.domain.enums.TipoFornecedor;

public class FornecedorDTO extends UsuarioDTO {
    private TipoFornecedor tipo;

    public TipoFornecedor getTipo() {
        return tipo;
    }

    public void setTipo(TipoFornecedor tipo) {
        this.tipo = tipo;
    }
}

