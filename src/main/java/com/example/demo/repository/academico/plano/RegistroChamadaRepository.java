package com.example.demo.repository.academico.plano;

import com.example.demo.domain.model.academico.plano.chamada.RegistroChamada;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

import java.util.Optional;
import java.util.UUID;

public interface RegistroChamadaRepository extends BaseRepository<RegistroChamada, Long> {
    Optional<RegistroChamada> findByUuid(UUID uuid);
    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);
    @Query("""
            SELECT rc FROM RegistroChamada rc
            JOIN rc.planoDisciplina pd
            WHERE pd.uuid = :planoDisciplinaUuid
              AND rc.professor.id = :professorId
            """)
    <T> Page<T> findAllByPlanoDisciplinaUuidAndProfessorId(
            @Param("planoDisciplinaUuid") UUID planoDisciplinaUuid,
            @Param("professorId") Long professorId,
            Pageable pageable,
            Class<T> projection);

    @Query("""
            SELECT rc FROM RegistroChamada rc
            JOIN rc.planoDisciplina pd
            WHERE pd.uuid = :planoDisciplinaUuid
              AND rc.professor.id = :professorId
              AND rc.dataAula = :dataAula
            """)
    <T> Optional<T> findByPlanoDisciplinaUuidAndProfessorIdAndDataAula(
            @Param("planoDisciplinaUuid") UUID planoDisciplinaUuid,
            @Param("professorId") Long professorId,
            @Param("dataAula") LocalDate dataAula,
            Class<T> projection);

    @Query("""
            SELECT CASE WHEN COUNT(rc) > 0 THEN TRUE ELSE FALSE END FROM RegistroChamada rc
            JOIN rc.planoDisciplina pd
            WHERE pd.uuid = :planoDisciplinaUuid AND rc.professor.id = :professorId AND rc.dataAula = :dataAula
            """)
    boolean existsByPlanoDisciplinaUuidAndProfessorIdAndDataAula(
            @Param("planoDisciplinaUuid") UUID planoDisciplinaUuid,
            @Param("professorId") Long professorId,
            @Param("dataAula") LocalDate dataAula);
}
