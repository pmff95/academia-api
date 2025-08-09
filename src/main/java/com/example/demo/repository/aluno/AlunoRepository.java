package com.example.demo.repository.aluno;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlunoRepository extends BaseRepository<Aluno, Long> {

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projectionClass);

    Optional<Aluno> findByUuid(UUID uuid);

    @Query("""
        SELECT a FROM Aluno a
        LEFT JOIN FETCH a.responsaveis ra
        LEFT JOIN FETCH ra.responsavel
        WHERE a.uuid = :uuid
    """)
    Optional<Aluno> findWithResponsaveisByUuid(@Param("uuid") UUID uuid);

    boolean existsByEmailAndEscolaUuid(String email, UUID escolaUuid);

    boolean existsByCpfAndEscolaUuid(String cpf, UUID escolaUuid);

    List<Aluno> findAllByUuidIn(List<UUID> uuids);

    List<Aluno> findAllByTurmasContaining(Turma turma);

}
