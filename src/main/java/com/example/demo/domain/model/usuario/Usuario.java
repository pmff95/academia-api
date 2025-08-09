package com.example.demo.domain.model.usuario;

import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.converter.CpfConverter;
import com.example.demo.domain.converter.TelefoneConverter;
import com.example.demo.domain.enums.MetodoAutenticacao;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_escola_cpf",
                        columnNames = {"escola_id", "cpf"}),
                @UniqueConstraint(name = "uk_usuario_escola_email",
                        columnNames = {"escola_id", "email"})
        })
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "matricula", unique = true, nullable = false)
    private String matricula;

    @Column(name = "matricula_manual")
    private String matriculaManual;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Convert(converter = CpfConverter.class)
    @Column(name = "cpf")
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo")
    private GrupoSanguineo grupoSanguineo;

//    @Convert(converter = TelefoneConverter.class)
    @Column(name = "telefone", unique = true)
    private String telefone;

    @Column(name = "ultimo_acesso")
    @NotAudited
    private LocalDateTime ultimoAcesso;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_autenticacao", nullable = false)
    private MetodoAutenticacao metodoAutenticacao = MetodoAutenticacao.SENHA;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "foto")
    private String foto;

    @Column(name = "primeiro_acesso")
    private Boolean primeiroAcesso = false;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Endereco endereco;

    @Column(name = "pai")
    private String pai;

    @Column(name = "mae")
    private String mae;

    @Version
    private int version;

    public Usuario() {
    }

    public UsuarioLogado toUsuarioLogado() {
        return new UsuarioLogado(this);
    }

}
