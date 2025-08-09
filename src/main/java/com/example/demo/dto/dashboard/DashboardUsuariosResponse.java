package com.example.demo.dto.dashboard;

public record DashboardUsuariosResponse(
        long totalUsuarios,
        Grupo alunos,
        Grupo professores
) {
    public record Grupo(long total, long masculino, long feminino) {}
}
