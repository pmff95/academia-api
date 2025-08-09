package com.example.demo.dto.usuario;

import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.NomeModulo;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.parametro.ParamsView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CurrentUserView(
        String nome,
        String email,
        String cpf,
        Boolean primeiroAcesso,
        Perfil perfil,
        Status status,
        String matricula,
        String telefone,
        String matriculaManual,
        String escolaNome,
        List<ParamsView> params,
        String logoUrl,
        String fotoUrl,
        GrupoSanguineo grupoSanguineo,
        LocalDate dataNascimento,
        List<NomeModulo> modulos,
        UUID uuid
) {
}
