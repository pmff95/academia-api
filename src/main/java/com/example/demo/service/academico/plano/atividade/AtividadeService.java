package com.example.demo.service.academico.plano.atividade;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.intinerario.AlunoGrupoItinerario;
import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.academico.plano.atividade.Atividade;
import com.example.demo.domain.model.academico.plano.atividade.EntregaAtividade;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.dto.academico.plano.AtividadeRequest;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.projection.AtividadeAlunoSummary;
import com.example.demo.dto.projection.AtividadeSummary;
import com.example.demo.repository.academico.plano.AtividadeRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.service.academico.plano.PlanoDisciplinaService;
import com.example.demo.service.common.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository repository;
    private final PlanoDisciplinaService planoDisciplinaService;
    private final AlunoRepository alunoRepository;
    private final StorageService storageService;

    @Transactional
    public UUID create(AtividadeRequest request) {
        PlanoDisciplina planoDisciplina = planoDisciplinaService.findByUuid(request.planoDisciplinaUuid());

        Atividade atividade = new Atividade();
        atividade.setTitulo(request.titulo());
        atividade.setDescricao(request.descricao());
        atividade.setPrazo(request.prazo());
        atividade.setPlanoDisciplina(planoDisciplina);
        atividade.setStatus(Status.ATIVO);

        String arquivoUrl = null;

        try {
            arquivoUrl = processarArquivo(atividade, request.arquivo());
            atividade.setArquivoUrl(arquivoUrl);

            List<Aluno> alunos;
            if (request.todosAlunos()) {
                if (planoDisciplina.isItinerario()) {
                    alunos = planoDisciplina
                            .getDisciplinaGrupo()
                            .getGrupo()
                            .getAlunos()
                            .stream()
                            .map(AlunoGrupoItinerario::getAluno)
                            .toList();
                } else {
                    alunos = planoDisciplina.getTurmaDisciplina().getTurma().getAlunos();
                }
            } else {
                alunos = alunoRepository.findAllByUuidIn(request.alunosSelecionados());
            }

            List<EntregaAtividade> entregas = alunos.stream().map(aluno -> {
                EntregaAtividade entrega = new EntregaAtividade();
                entrega.setAluno(aluno);
                entrega.setStatus(Status.ATIVO);
                entrega.setAtividade(atividade);
                return entrega;
            }).toList();

            atividade.setEntregaAtividades(entregas);

            repository.save(atividade);
            return atividade.getUuid();

        } catch (Exception e) {

            if (arquivoUrl != null && !arquivoUrl.isBlank()) {
                String key = extrairKeyDaUrl(arquivoUrl);
                storageService.deleteFile(key);
            }
            throw EurekaException.ofException("Erro ao criar atividade: " + e.getMessage());
        }
    }


    @Transactional
    public void update(UUID uuid, AtividadeRequest request) {
        Atividade atividade = repository
                .findByUuid(uuid, Atividade.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Atividade não encontrada."));
        atividade.setTitulo(request.titulo());
        atividade.setDescricao(request.descricao());
        atividade.setPrazo(request.prazo());
        repository.save(atividade);
    }

    @Transactional
    public void changeStatus(UUID uuid, Status status) {
        Atividade atividade = repository
                .findByUuid(uuid, Atividade.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Atividade não encontrada."));
        atividade.setStatus(status);
        repository.save(atividade);
    }

    public Page<AtividadeSummary> findAll(UUID planoDisciplinaUuid, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by("prazo");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return repository.findAllByPlanoDisciplinaUuid(
                planoDisciplinaUuid, sortedPageable, AtividadeSummary.class);
    }

    public Page<AtividadeAlunoSummary> findAllAluno(UUID planoDisciplinaUuid, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by("prazo");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();

        return repository.findAllByPlanoDisciplinaUuidAndAlunoUuid(
                planoDisciplinaUuid,
                usuarioLogado.getUuid(),
                sortedPageable);
    }

    public <T> T findByUuid(UUID uuid, Class<T> projection) {
        return repository
                .findByUuid(uuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Atividade não encontrada."));
    }

    private String processarArquivo(Atividade atividade, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            return "";
        }

        StorageOutput output = storageService.uploadFile(arquivo);
        if (output != null && output.getUrl() != null && !output.getUrl().isBlank()) {
            atividade.setArquivoUrl(output.getUrl());
            return output.getUrl();
        }

        return "";
    }

    private String extrairKeyDaUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
