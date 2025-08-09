package com.example.demo.service.academico.itinerario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.academico.intinerario.ItinerarioFormativo;
import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.ItinerarioFormativoRequest;
import com.example.demo.dto.projection.ItinerarioFormativoSummary;
import com.example.demo.repository.academico.ItinerarioFormativoRepository;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
import com.example.demo.repository.specification.ItinerarioFormativoSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.LocalDate;

@Service
public class ItinerarioFormativoService {

    private final ItinerarioFormativoRepository repository;
    private final GrupoItinerarioRepository grupoRepository;

    public ItinerarioFormativoService(ItinerarioFormativoRepository repository,
                                      GrupoItinerarioRepository grupoRepository) {
        this.repository = repository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public UUID create(ItinerarioFormativoRequest request) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        ItinerarioFormativo itinerario = new ItinerarioFormativo();
        itinerario.setNome(request.nome());
        itinerario.setDescricao(request.descricao());
        itinerario.setEscola(usuario.getEscola());
        repository.save(itinerario);

        GrupoItinerario grupo = new GrupoItinerario();
        grupo.setNome("Grupo 1");
        grupo.setAnoLetivo(LocalDate.now().getYear());
        grupo.setItinerario(itinerario);
        grupo.setEscola(usuario.getEscola());
        grupo.setStatus(Status.ATIVO);
        grupoRepository.save(grupo);
        return itinerario.getUuid();
    }

    @Transactional
    public void update(UUID uuid, ItinerarioFormativoRequest request) {
        ItinerarioFormativo itinerario = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Itinerário não encontrado."));
        itinerario.setNome(request.nome());
        itinerario.setDescricao(request.descricao());
        repository.save(itinerario);
    }

    public Page<ItinerarioFormativoSummary> findAll(ItinerarioFormativoSpecification specification, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by("nome");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllProjected(specification, sortedPageable, ItinerarioFormativoSummary.class);
    }

    public <T> T findByUuid(UUID uuid, Class<T> projection) {
        return repository.findByUuid(uuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Itinerário não encontrado."));
    }
}
