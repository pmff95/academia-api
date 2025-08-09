package com.example.demo.repository.carteira;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.carteira.Cartao;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartaoRepository extends BaseRepository<Cartao, Long> {
    Optional<Cartao> findByStatusAndCarteira_Aluno_Uuid(Status status, UUID alunoUuid);

    Optional<Cartao> findByStatusAndCarteira_Professor_Uuid(Status status, UUID professorUuid);

}
