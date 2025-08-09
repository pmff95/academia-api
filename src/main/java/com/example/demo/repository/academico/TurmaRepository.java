package com.example.demo.repository.academico;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Turno;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TurmaRepository extends BaseRepository<Turma, Long> {
    Optional<Turma> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> type);

    <T> Page<T> findAllProjectedBy(Pageable pageable, Class<T> type);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndTurno(String nome, Turno turno);

    @Query("""
            SELECT t.uuid AS uuid,
                   t.nome AS nome,
                   t.serie AS serie,
                   t.turno AS turno,
                   t.limiteVagas AS limiteVagas,
                   COUNT(a) AS quantidadeAlunos
            FROM Turma t
            LEFT JOIN t.alunos a
            WHERE t.serie = :serie
            GROUP BY t.id
            """)
    <T> List<T> findAllBySerie(Serie serie, Class<T> projection);

    @Query("""
    SELECT a.uuid AS uuid,
           a.nome AS nome,
           a.foto AS foto,
           a.matricula AS matricula,
           a.cpf AS cpf,
           a.dataNascimento AS dataNascimento,
           t.serie AS serie,
           a.status AS status
    FROM Turma t
    JOIN t.alunos a
    WHERE t.uuid = :turmaUuid
    """)
    <T> List<T> findAlunosByTurmaUuid(@Param("turmaUuid") UUID turmaUuid, Class<T> projection);

}
