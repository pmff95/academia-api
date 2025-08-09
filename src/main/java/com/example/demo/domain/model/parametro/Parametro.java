package com.example.demo.domain.model.parametro;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.enums.TipoValor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "parametro")
@Getter
@Setter
public class Parametro extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chave", nullable = false, unique = true)
    private String chave;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoParametro tipo = TipoParametro.OUTRO;

    @Column(name = "valor_default")
    private String valorDefault;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_valor", nullable = false)
    private TipoValor tipoValor = TipoValor.TEXTO;

    @Column(name = "necessario_confirmacao")
    private Boolean necessarioConfirmacao = Boolean.FALSE;

    @Column(name = "mensagem_confirmacao_ativo")
    private String mensagemConfirmacaoAtivo;

    @Column(name = "mensagem_confirmacao_inativo")
    private String mensagemConfirmacaoInativo;

    @Column(name = "mensagem_confirmacao_alteracao")
    private String mensagemConfirmacaoAlteracao;

    @Version
    private int version;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (chave != null) {
            chave = chave.trim().toLowerCase();
            chave = chave.replaceAll("[^a-z0-9]+", "_");
            chave = chave.replaceAll("_+", "_");
            if (chave.startsWith("_")) {
                chave = chave.substring(1);
            }
            if (chave.endsWith("_")) {
                chave = chave.substring(0, chave.length() - 1);
            }
        }
    }
}
