package com.example.demo.service.academico.plano.chamada;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.academico.plano.chamada.ItemChamada;
import com.example.demo.domain.model.academico.plano.chamada.RegistroChamada;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.dto.academico.plano.chamada.RegistroChamadaItemRequest;
import com.example.demo.dto.academico.plano.chamada.RegistroChamadaRequest;
import com.example.demo.dto.projection.RegistroChamadaSummary;
import com.example.demo.dto.projection.chamada.RegistroChamadaView;
import com.example.demo.dto.projection.chamada.RegistroChamadaViewImpl;
import com.example.demo.repository.academico.DisciplinaGrupoItinerarioRepository;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.academico.plano.RegistroChamadaRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.professor.ProfessorRepository;
import com.example.demo.service.academico.plano.PlanoDisciplinaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistroChamadaService {

    private final RegistroChamadaRepository repository;
    private final PlanoDisciplinaService planoDisciplinaService;
    private final DisciplinaGrupoItinerarioRepository disciplinaGrupoItinerarioRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public UUID create(RegistroChamadaRequest request) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        Professor professor = professorRepository.findById(currentUser.getId())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));

        PlanoDisciplina planoDisciplina = planoDisciplinaService.findByUuid(request.planoDisciplinaUuid());

        RegistroChamada chamada = new RegistroChamada();
        chamada.setPlanoDisciplina(planoDisciplina);
        chamada.setProfessor(professor);
        chamada.setDataAula(request.dataAula());
        chamada.setAulaDupla(Boolean.TRUE.equals(request.aulaDupla()));
        chamada.setPreenchido(true);

        List<ItemChamada> itens = request.presencas().stream().map(this::toItem).toList();
        itens.forEach(i -> i.setRegistroChamada(chamada));
        chamada.setPresencas(itens);

        repository.save(chamada);
        return chamada.getUuid();
    }

    @Transactional
    public void update(UUID uuid, RegistroChamadaRequest request) {
        RegistroChamada chamada = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro de chamada não encontrado."));

        chamada.setDataAula(request.dataAula());
        chamada.setAulaDupla(Boolean.TRUE.equals(request.aulaDupla()));
        chamada.getPresencas().clear();
        List<ItemChamada> itens = request.presencas().stream().map(this::toItem).toList();
        itens.forEach(i -> i.setRegistroChamada(chamada));
        chamada.getPresencas().addAll(itens);

        repository.save(chamada);
    }

    public Page<RegistroChamadaSummary> findAll(UUID planoDisciplinaUuid, Pageable pageable) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();

        return repository
                .findAllByPlanoDisciplinaUuidAndProfessorId(
                        planoDisciplinaUuid,
                        currentUser.getId(),
                        pageable,
                        RegistroChamadaSummary.class);
    }

    public RegistroChamadaView findByDate(UUID planoDisciplinaUuid, LocalDate dataAula) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        boolean registrado = repository.existsByPlanoDisciplinaUuidAndProfessorIdAndDataAula(
                planoDisciplinaUuid,
                currentUser.getId(),
                dataAula);

        RegistroChamadaView view = repository
                .findByPlanoDisciplinaUuidAndProfessorIdAndDataAula(
                        planoDisciplinaUuid,
                        currentUser.getId(),
                        dataAula,
                        RegistroChamadaView.class)
                .orElse(null);

        return new RegistroChamadaViewImpl(view, registrado);
    }

    private ItemChamada toItem(RegistroChamadaItemRequest r) {
        Aluno aluno = alunoRepository.findByUuid(r.alunoUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Aluno não encontrado."));
        ItemChamada item = new ItemChamada();
        item.setAluno(aluno);
        item.setPresente(Boolean.TRUE.equals(r.presente()));
        return item;
    }
}
