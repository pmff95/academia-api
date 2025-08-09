package com.example.demo.service.academico.plano.atividade;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.academico.plano.atividade.EntregaAtividade;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.projection.EntregaAtividadeAlunoView;
import com.example.demo.dto.projection.EntregaAtividadeAlunoViewImpl;
import com.example.demo.dto.projection.aluno.AlunoSummary;
import com.example.demo.dto.projection.aluno.AlunoSummaryImpl;
import com.example.demo.repository.academico.TurmaRepository;
import com.example.demo.repository.academico.plano.EntregaAtividadeRepository;
import com.example.demo.service.common.StorageService;
import com.example.demo.dto.academico.plano.RegistrarNotaItemRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntregaAtividadeService {

    private final EntregaAtividadeRepository repository;
    private final StorageService storageService;
    private final TurmaRepository turmaRepository;

    @Transactional
    public void entregar(UUID atividadeUuid, MultipartFile arquivo) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        if (usuarioLogado == null) throw EurekaException.ofNotFound("Usuário não encontrado");

        EntregaAtividade entrega = repository
                .findByAtividade_UuidAndAluno_Uuid(atividadeUuid, usuarioLogado.getUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Entrega não encontrada."));

        StorageOutput output = storageService.uploadFile(arquivo);
        if (output != null) {
            entrega.setArquivoUrl(output.getUrl());
        }
        entrega.setEntregueEm(LocalDateTime.now());
        repository.save(entrega);
    }

    public List<EntregaAtividadeAlunoViewImpl> listarEntregas(UUID atividadeUuid, UUID turmaUuid) {
        var views = repository.findAllByAtividade_Uuid(atividadeUuid, EntregaAtividadeAlunoView.class);
        Map<UUID, EntregaAtividadeAlunoViewImpl> resultado = new LinkedHashMap<>();
        for (EntregaAtividadeAlunoView view : views) {
            var impl = new EntregaAtividadeAlunoViewImpl(view);
            resultado.put(view.getAluno().getUuid(), impl);
        }

        if (turmaUuid != null) {
            var alunos = turmaRepository.findAlunosByTurmaUuid(turmaUuid, AlunoSummary.class);
            for (AlunoSummary aluno : alunos) {
                resultado.computeIfAbsent(aluno.getUuid(),
                        uuid -> new EntregaAtividadeAlunoViewImpl(
                                null,
                                new AlunoSummaryImpl(aluno),
                                null,
                                null,
                                null,
                                false
                        ));
            }
        }

        return new ArrayList<>(resultado.values());
    }

    @Transactional
    public void registrarNotas(UUID atividadeUuid, List<RegistrarNotaItemRequest> notas) {
        for (RegistrarNotaItemRequest r : notas) {
            EntregaAtividade entrega = repository
                    .findByAtividade_UuidAndAluno_Uuid(atividadeUuid, r.alunoUuid())
                    .orElseThrow(() -> EurekaException.ofNotFound("Entrega não encontrada."));
            entrega.setNota(r.nota());
            repository.save(entrega);
        }
    }
}
