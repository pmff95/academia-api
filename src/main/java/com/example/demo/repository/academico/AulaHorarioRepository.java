package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.AulaHorario;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AulaHorarioRepository extends BaseRepository<AulaHorario, Long> {
    Optional<AulaHorario> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    <T> Optional<T> findByUuidAndTurmaDisciplinaUuid(UUID uuid, UUID turmaDisciplinaUuid, Class<T> projection);

    <T> Page<T> findAllByTurmaDisciplinaUuid(UUID turmaDisciplinaUuid, Pageable pageable, Class<T> projection);

    @org.springframework.data.jpa.repository.Query("""
            SELECT td.uuid AS turmaDisciplinaUuid,
                   sha.ordem AS ordem,
                   d.nome  AS disciplinaNome,
                   p.nome  AS professorNome,
                   ah.dia  AS dia
            FROM AulaHorario ah
            JOIN ah.turmaDisciplina td
            JOIN td.disciplina d
            JOIN td.professor p
            JOIN td.turma t
            JOIN SerieHorarioAula sha ON sha.serie = t.serie
                                      AND sha.inicio = ah.inicio
                                      AND sha.fim = ah.fim
            WHERE t.uuid = :turmaUuid
            ORDER BY ah.dia, sha.ordem
            """)
    <T> java.util.List<T> findHorarioByTurmaUuid(java.util.UUID turmaUuid, Class<T> projection);
}
