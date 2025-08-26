package com.example.demo.entity;

import com.example.demo.domain.enums.TipoFornecedor;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("FORNECEDOR")
public class Fornecedor extends Usuario {
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_fornecedor", nullable = true)
    private TipoFornecedor tipo;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos;
}

