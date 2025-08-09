package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.TurmaDisciplina;
import com.example.demo.dto.projection.TurmaDisciplinaView;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TurmaDisciplinaRepository extends BaseRepository<TurmaDisciplina, Long> {

    Optional<TurmaDisciplina> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    <T> Optional<T> findByUuidAndTurmaUuid(UUID uuid, UUID turmaUuid, Class<T> projection);

    Optional<TurmaDisciplina> findByTurma_UuidAndDisciplina_Uuid(UUID turmaUuid, UUID disciplinaUuid);
    Optional<TurmaDisciplina> findFirstByDisciplinaUuid(UUID disciplinaUuid);

    <T> Page<T> findAllByTurmaUuid(UUID turmaUuid, Pageable pageable, Class<T> projection);

    <T> List<T> findAllByTurmaUuid(UUID turmaUuid, Class<T> projection);

    Optional<TurmaDisciplina> findByTurma_UuidAndProfessor_Id(UUID turmaUuid, Long professorId);

    @Query("""
        SELECT DISTINCT d.uuid AS disciplinaUuid,
                        d.nome AS disciplinaNome,
                        d.nivelEnsino AS disciplinaNivelEnsino,
                        t.serie AS serie
        FROM TurmaDisciplina td
        JOIN td.disciplina d
        JOIN td.turma t
        WHERE td.professor.uuid = :professorUuid
    """)
    <T> List<T> findDisciplinasByProfessorUuid(UUID professorUuid, Class<T> projection);

    @Query("""
        SELECT td.uuid AS uuid,
               t.uuid AS turmaUuid,
               d.uuid AS disciplinaUuid,
               d.nome AS disciplinaNome,
               p.uuid AS professorUuid,
               p.nome AS professorNome,
               d.cargaHoraria AS cargaHoraria,
               g.uuid AS grupoUuid,
               g.nome AS grupoNome,
               CASE WHEN g.uuid IS NOT NULL THEN TRUE ELSE FALSE END AS itinerario
        FROM TurmaDisciplina td
        JOIN td.turma t
        JOIN t.alunos a
        LEFT JOIN td.professor p
        JOIN td.disciplina d
        LEFT JOIN DisciplinaGrupoItinerario dgi ON dgi.disciplina = d
        LEFT JOIN dgi.grupo g
        LEFT JOIN AlunoGrupoItinerario agi ON agi.grupo = g AND agi.aluno = a
        WHERE t.uuid = :turmaUuid
          AND a.uuid = :alunoUuid
    """)
    <T> List<T> findDisciplinasByAlunoUuidAndTurmaUuid(@Param("alunoUuid") UUID alunoUuid,
                                                       @Param("turmaUuid") UUID turmaUuid,
                                                       Class<T> projection);

    @Query("""
        SELECT td.uuid AS uuid,
               t.uuid AS turmaUuid,
               d.uuid AS disciplinaUuid,
               d.nome AS disciplinaNome,
               p.uuid AS professorUuid,
               p.nome AS professorNome,
               d.cargaHoraria AS cargaHoraria,
               g.uuid AS grupoUuid,
               g.nome AS grupoNome,
               CASE WHEN g.uuid IS NOT NULL THEN TRUE ELSE FALSE END AS itinerario
        FROM TurmaDisciplina td
        JOIN td.turma t
        JOIN t.alunos a
        LEFT JOIN td.professor p
        JOIN td.disciplina d
        LEFT JOIN DisciplinaGrupoItinerario dgi ON dgi.disciplina = d
        LEFT JOIN dgi.grupo g
        LEFT JOIN AlunoGrupoItinerario agi ON agi.grupo = g AND agi.aluno = a
        WHERE a.uuid = :alunoUuid
    """)
    <T> List<T> findDisciplinasByAlunoUuid(@Param("alunoUuid") UUID alunoUuid,
                                           Class<T> projection);

    @Query("""
        SELECT
            d.uuid AS disciplinaUuid,
            t.uuid AS turmaUuid,
            t.nome AS nomeTurma,
            d.nome AS nomeDisciplina,
            d.nivelEnsino AS nivelEnsino,
            t.serie AS serie,
            pd.uuid AS planoDisciplinaUuid
        FROM TurmaDisciplina td
        JOIN td.turma t
        JOIN td.disciplina d
        JOIN PlanoDisciplina pd ON pd.turmaDisciplina.id = td.id 
        WHERE td.professor.id = :professorId
    """)
    List<TurmaDisciplinaView> buscarSugestoesPorProfessorId(@Param("professorId") Long professorId);
}
