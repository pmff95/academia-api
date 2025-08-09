package com.example.demo.domain.model.professor;

import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professor")
@Audited
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class Professor extends Usuario {

    @ManyToMany
    @JoinTable(
            name = "professor_disciplina",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private List<Disciplina> disciplinas = new ArrayList<>();

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioDisponivel> horariosDisponiveis = new ArrayList<>();
}
