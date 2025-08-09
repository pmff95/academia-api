package com.example.demo.repository.aluno;

import com.example.demo.domain.model.aluno.ResponsavelAluno;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResponsavelAlunoRepository extends BaseRepository<ResponsavelAluno, Long> {

    @Query("""
                SELECT COUNT(ra) > 0
                FROM ResponsavelAluno ra
                WHERE ra.responsavel.id = :responsavelId
                  AND ra.aluno.id = :alunoId
            """)
    boolean existsByResponsavelAndAluno(@Param("responsavelId") Long responsavelId,
                                        @Param("alunoId") Long alunoId);

    @Query("""
                SELECT COUNT(ra) > 0
                FROM ResponsavelAluno ra
                JOIN ra.responsavel u
                WHERE ra.aluno.uuid = :alunoId
                  AND u.primeiroAcesso IS TRUE
            """)
    Boolean existsPeloMenosUmResponsavelComPrimeiroAcesso(@Param("alunoId") UUID alunoId);
    java.util.List<ResponsavelAluno> findByAluno_Uuid(java.util.UUID alunoUuid);

    java.util.Optional<ResponsavelAluno> findByResponsavel_IdAndAluno_Id(Long responsavelId, Long alunoId);


}
