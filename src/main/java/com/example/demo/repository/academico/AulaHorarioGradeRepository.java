package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.AulaHorarioGrade;
import com.example.demo.repository.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface AulaHorarioGradeRepository extends BaseRepository<AulaHorarioGrade, Long> {
    Optional<AulaHorarioGrade> findByUuid(UUID uuid);

    java.util.List<AulaHorarioGrade> findByTurma_Uuid(UUID turmaUuid);

    void deleteByTurma_Uuid(UUID turmaUuid);

    @org.springframework.data.jpa.repository.Query("""
            SELECT d.uuid  AS disciplinaUuid,
                   sha.ordem AS ordem,
                   d.nome  AS disciplinaNome,
                   p.nome  AS professorNome,
                   ah.dia  AS dia
            FROM AulaHorarioGrade ah
            JOIN ah.turma t
            JOIN ah.disciplina d
            LEFT JOIN ah.grupo g
            LEFT JOIN TurmaDisciplina td ON td.turma = t AND td.disciplina = d
            LEFT JOIN td.professor p
            JOIN SerieHorarioAula sha ON sha.serie = t.serie
                                      AND sha.inicio = ah.inicio
                                      AND sha.fim = ah.fim
            WHERE t.uuid = :turmaUuid AND g IS NULL
            ORDER BY ah.dia, sha.ordem
            """)
    <T> java.util.List<T> findHorarioByTurmaUuid(java.util.UUID turmaUuid, Class<T> projection);

    @org.springframework.data.jpa.repository.Query("""
            SELECT d.uuid  AS disciplinaUuid,
                   sha.ordem AS ordem,
                   d.nome  AS disciplinaNome,
                   pi.nome AS professorNome,
                   ah.dia  AS dia
            FROM AulaHorarioGrade ah
            JOIN ah.turma t
            JOIN ah.disciplina d
            JOIN ah.grupo g
            JOIN DisciplinaGrupoItinerario dgi ON dgi.grupo = g AND dgi.disciplina = d
            LEFT JOIN dgi.professor pi
            JOIN SerieHorarioAula sha ON sha.serie = t.serie
                                      AND sha.inicio = ah.inicio
                                      AND sha.fim = ah.fim
            WHERE t.uuid = :turmaUuid AND g.uuid = :grupoUuid
            ORDER BY ah.dia, sha.ordem
            """)
    <T> java.util.List<T> findHorarioByTurmaUuidAndGrupoUuid(java.util.UUID turmaUuid, java.util.UUID grupoUuid, Class<T> projection);

    @org.springframework.data.jpa.repository.Query("""
            SELECT t.uuid  AS turmaUuid,
                   t.nome  AS turmaNome,
                   d.uuid  AS disciplinaUuid,
                   d.nome  AS disciplinaNome,
                   sha.ordem AS ordem,
                   ah.dia  AS dia,
                   sha.inicio AS inicio,
                   sha.fim AS fim
            FROM AulaHorarioGrade ah
            JOIN ah.turma t
            JOIN ah.disciplina d
            LEFT JOIN ah.grupo g
            LEFT JOIN TurmaDisciplina td ON td.turma = t AND td.disciplina = d
            LEFT JOIN td.professor p
            JOIN SerieHorarioAula sha ON sha.serie = t.serie
                                      AND sha.inicio = ah.inicio
                                      AND sha.fim = ah.fim
            WHERE p.uuid = :professorUuid AND g IS NULL
            ORDER BY ah.dia, sha.ordem
            """)
    <T> java.util.List<T> findHorarioByProfessorUuid(java.util.UUID professorUuid, Class<T> projection);

    @org.springframework.data.jpa.repository.Query("""
            SELECT t.uuid  AS turmaUuid,
                   t.nome  AS turmaNome,
                   d.uuid  AS disciplinaUuid,
                   d.nome  AS disciplinaNome,
                   g.uuid  AS grupoUuid,
                   g.nome  AS grupoNome,
                   sha.ordem AS ordem,
                   ah.dia  AS dia,
                   sha.inicio AS inicio,
                   sha.fim AS fim
            FROM AulaHorarioGrade ah
            JOIN ah.turma t
            JOIN ah.disciplina d
            JOIN ah.grupo g
            JOIN DisciplinaGrupoItinerario dgi ON dgi.grupo = g AND dgi.disciplina = d
            LEFT JOIN dgi.professor pi
            JOIN SerieHorarioAula sha ON sha.serie = t.serie
                                      AND sha.inicio = ah.inicio
                                      AND sha.fim = ah.fim
            WHERE pi.uuid = :professorUuid
            ORDER BY ah.dia, sha.ordem
            """)
    <T> java.util.List<T> findHorarioItinerarioByProfessorUuid(java.util.UUID professorUuid, Class<T> projection);
}
