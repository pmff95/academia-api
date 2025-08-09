package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.GrauAlergia;
import com.example.demo.domain.enums.TipoAlergia;

import java.time.LocalDate;
import java.util.UUID;

public record AlergiaResponse(
        UUID uuid,
        TipoAlergia tipo,
        String substancia,
        GrauAlergia gravidade,
        String observacoes,
        LocalDate dataDiagnostico,
        Boolean cuidadosEmergenciais
) {
}
