package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.intinerario.DisciplinaGrupoItinerario;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplinaGrupoItinerarioRepository extends BaseRepository<DisciplinaGrupoItinerario, Long> {
    Optional<DisciplinaGrupoItinerario> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    <T> Optional<T> findByUuidAndGrupoUuid(UUID uuid, UUID grupoUuid, Class<T> projection);

    <T> Page<T> findAllByGrupoUuid(UUID grupoUuid, Pageable pageable, Class<T> projection);

    <T> List<T> findAllByGrupoUuid(UUID grupoUuid, Class<T> projection);

    @Query("""
        SELECT dgi FROM DisciplinaGrupoItinerario dgi
        WHERE dgi.grupo.uuid = :grupoUuid
          AND dgi.disciplina.uuid = :disciplinaUuid
          AND dgi.professor.id = :professorId
    """)
    Optional<DisciplinaGrupoItinerario> findByGrupoUuidAndDisciplinaUuidAndProfessorId(
            @Param("grupoUuid") UUID grupoUuid,
            @Param("disciplinaUuid") UUID disciplinaUuid,
            @Param("professorId") Long professorId);

    @Query("""
        SELECT dgi FROM DisciplinaGrupoItinerario dgi
        WHERE dgi.disciplina.uuid = :disciplinaUuid
          AND dgi.professor.id = :professorId
    """)
    Optional<DisciplinaGrupoItinerario> findByDisciplinaUuidAndProfessorId(
            @Param("disciplinaUuid") UUID disciplinaUuid,
            @Param("professorId") Long professorId);

    @Query("""
        SELECT dgi FROM DisciplinaGrupoItinerario dgi
        WHERE dgi.disciplina.uuid = :disciplinaUuid
    """)
    Optional<DisciplinaGrupoItinerario> findFirstByDisciplinaUuid(@Param("disciplinaUuid") UUID disciplinaUuid);
}
