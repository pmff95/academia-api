package com.example.demo.dto.professor;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.GrupoSanguineo;
import java.time.LocalDate;
import com.example.demo.dto.projection.HorarioDisponivelSummary;
import com.example.demo.dto.usuario.EnderecoResponse;

import java.util.List;
import java.util.UUID;

public record ProfessorFullResponse(
        UUID uuid,
        String nome,
        String email,
        String cpf,
        GrupoSanguineo grupoSanguineo,
        String matricula,
        String foto,
        String telefone,
        LocalDate dataNascimento,
        Sexo sexo,
        Status status,
        String pai,
        String mae,
        EnderecoResponse endereco,
        String numeroCartao,
        List<HorarioDisponivelSummary> horariosDisponiveis,
        List<DisciplinaSerieResponse> disciplinas
) {
}
