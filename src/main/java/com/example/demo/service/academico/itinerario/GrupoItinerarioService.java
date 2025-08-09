package com.example.demo.service.academico.itinerario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.domain.model.academico.intinerario.ItinerarioFormativo;
import com.example.demo.dto.academico.GrupoItinerarioRequest;
import com.example.demo.dto.projection.GrupoItinerarioSummary;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
import com.example.demo.repository.academico.ItinerarioFormativoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
public class GrupoItinerarioService {

    private final GrupoItinerarioRepository repository;
    private final ItinerarioFormativoRepository itinerarioRepository;

    public GrupoItinerarioService(GrupoItinerarioRepository repository,
                                  ItinerarioFormativoRepository itinerarioRepository) {
        this.repository = repository;
        this.itinerarioRepository = itinerarioRepository;
    }

    @Transactional
    public UUID create(UUID itinerarioUuid, GrupoItinerarioRequest request) {
        ItinerarioFormativo itinerario = itinerarioRepository.findByUuid(itinerarioUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Itinerário não encontrado."));

        Integer anoLetivo = request.anoLetivo();
        if (anoLetivo == null) {
            anoLetivo = Year.now().getValue();
        }

        GrupoItinerario grupo = new GrupoItinerario();
        grupo.setNome(request.nome());
        grupo.setAnoLetivo(anoLetivo);
        grupo.setItinerario(itinerario);
        grupo.setEscola(itinerario.getEscola());
        grupo.setStatus(Status.ATIVO);
        repository.save(grupo);
        return grupo.getUuid();
    }

    @Transactional
    public void update(UUID itinerarioUuid, UUID uuid, GrupoItinerarioRequest request) {
        GrupoItinerario grupo = repository.findByUuidAndItinerarioUuid(uuid, itinerarioUuid, GrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
        grupo.setNome(request.nome());
        grupo.setAnoLetivo(request.anoLetivo());
        repository.save(grupo);
    }

    @Transactional
    public void changeStatus(UUID itinerarioUuid, UUID uuid, Status status) {
        GrupoItinerario grupo = repository.findByUuidAndItinerarioUuid(uuid, itinerarioUuid, GrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
        grupo.setStatus(status);
        repository.save(grupo);
    }

    public Page<GrupoItinerarioSummary> findAll(UUID itinerarioUuid, Pageable pageable) {
        return repository.findAllByItinerarioUuid(itinerarioUuid, pageable, GrupoItinerarioSummary.class);
    }

    public <T> T findByUuid(UUID itinerarioUuid, UUID uuid, Class<T> projection) {
        return repository.findByUuidAndItinerarioUuid(uuid, itinerarioUuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
    }
}
