package com.example.demo.service.academico.itinerario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.intinerario.AlunoGrupoItinerario;
import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.dto.academico.AlunoGrupoItinerarioRequest;
import com.example.demo.dto.projection.AlunoGrupoItinerarioSummary;
import com.example.demo.repository.academico.AlunoGrupoItinerarioRepository;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlunoGrupoItinerarioService {

    private final AlunoGrupoItinerarioRepository repository;
    private final GrupoItinerarioRepository grupoRepository;
    private final AlunoRepository alunoRepository;

    public AlunoGrupoItinerarioService(AlunoGrupoItinerarioRepository repository,
                                       GrupoItinerarioRepository grupoRepository,
                                       AlunoRepository alunoRepository) {
        this.repository = repository;
        this.grupoRepository = grupoRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public UUID create(UUID itinerarioUuid, UUID grupoUuid, AlunoGrupoItinerarioRequest request) {
        GrupoItinerario grupo = grupoRepository.findByUuidAndItinerarioUuid(grupoUuid, itinerarioUuid, GrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
        Aluno aluno = alunoRepository.findByUuid(request.alunoUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Aluno não encontrado."));
        AlunoGrupoItinerario agi = new AlunoGrupoItinerario();
        agi.setGrupo(grupo);
        agi.setAluno(aluno);
        agi.setStatus(Status.ATIVO);
        repository.save(agi);
        return agi.getUuid();
    }

    @Transactional
    public void update(UUID itinerarioUuid, UUID grupoUuid, UUID uuid, AlunoGrupoItinerarioRequest request) {
        AlunoGrupoItinerario agi = repository.findByUuidAndGrupoUuid(uuid, grupoUuid, AlunoGrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
        Aluno aluno = alunoRepository.findByUuid(request.alunoUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Aluno não encontrado."));
        agi.setAluno(aluno);
        repository.save(agi);
    }

    @Transactional
    public void changeStatus(UUID itinerarioUuid, UUID grupoUuid, UUID uuid, Status status) {
        AlunoGrupoItinerario agi = repository.findByUuidAndGrupoUuid(uuid, grupoUuid, AlunoGrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
        agi.setStatus(status);
        repository.save(agi);
    }

    public Page<AlunoGrupoItinerarioSummary> findAll(UUID grupoUuid, Pageable pageable) {
        return repository.findAllByGrupoUuid(grupoUuid, pageable, AlunoGrupoItinerarioSummary.class);
    }

    public <T> T findByUuid(UUID grupoUuid, UUID uuid, Class<T> projection) {
        return repository.findByUuidAndGrupoUuid(uuid, grupoUuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
    }
}
