package com.example.demo.event;

import com.example.demo.domain.model.professor.Professor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfessorCreatedEvent {
    private final Professor professor;
    private final String numeroCartao;
    private final String senhaUsuario;
    private final String senhaCartao;
}
