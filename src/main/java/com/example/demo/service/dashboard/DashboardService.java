package com.example.demo.service.dashboard;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.dashboard.DashboardUsuariosResponse;
import com.example.demo.dto.projection.dashboard.AniversarianteView;
import com.example.demo.dto.projection.dashboard.PerfilSexoCountView;
import com.example.demo.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;

    public List<AniversarianteView> buscarAniversariantesDoMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw EurekaException.ofValidation("Mês inválido. Informe um valor entre 1 e 12.");
        }
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        UUID escolaId = usuarioLogado.getEscola().getUuid();
        return usuarioRepository.findAniversariantesByMes(escolaId, mes);
    }

    public DashboardUsuariosResponse obterResumoUsuarios() {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        UUID escolaId = usuarioLogado.getEscola().getUuid();

        long totalUsuarios = usuarioRepository.countByEscolaUuidAndStatus(escolaId, Status.ATIVO);
        List<PerfilSexoCountView> counts = usuarioRepository.countByPerfilAndSexo(escolaId);

        long alunosMasculino = getCount(counts, Perfil.ALUNO, Sexo.MASCULINO);
        long alunosFeminino = getCount(counts, Perfil.ALUNO, Sexo.FEMININO);
        long professoresMasculino = getCount(counts, Perfil.PROFESSOR, Sexo.MASCULINO);
        long professoresFeminino = getCount(counts, Perfil.PROFESSOR, Sexo.FEMININO);

        DashboardUsuariosResponse.Grupo alunos = new DashboardUsuariosResponse.Grupo(
                alunosMasculino + alunosFeminino,
                alunosMasculino,
                alunosFeminino
        );
        DashboardUsuariosResponse.Grupo professores = new DashboardUsuariosResponse.Grupo(
                professoresMasculino + professoresFeminino,
                professoresMasculino,
                professoresFeminino
        );

        return new DashboardUsuariosResponse(totalUsuarios, alunos, professores);
    }

    private long getCount(List<PerfilSexoCountView> counts, Perfil perfil, Sexo sexo) {
        return counts.stream()
                .filter(c -> c.getPerfil() == perfil && c.getSexo() == sexo)
                .mapToLong(PerfilSexoCountView::getTotal)
                .findFirst()
                .orElse(0L);
    }
}
