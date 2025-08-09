package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.projection.TurmaSummary;

import java.util.List;
import java.util.UUID;

public record AlunoFullResponse(
        UUID uuid,
        String nome,
        String email,
        String cpf,
        Sexo sexo,
        String telefone,
        String dataNascimento,
        GrupoSanguineo grupoSanguineo,
        String matricula,
        String foto,
        Status status,
        String numeroCartao,
        List<ResponsavelAlunoResponse> responsaveis,
        List<TurmaSummary> turmas,
        List<AlergiaResponse> alergias,
        List<MedicamentoResponse> medicamentos
) {
}
