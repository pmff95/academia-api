package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.SerieHorarioItinerario;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieHorarioItinerarioRepository extends BaseRepository<SerieHorarioItinerario, Long> {
    List<SerieHorarioItinerario> findByTurma(Turma turma);

    @Query("""
            SELECT shi.uuid AS uuid,
                   shi.dia AS dia,
                   sha.ordem AS ordem,
                   sha.inicio AS inicio,
                   sha.fim AS fim
            FROM SerieHorarioItinerario shi
            JOIN SerieHorarioAula sha ON sha.serie = shi.turma.serie AND sha.ordem = shi.ordem
            WHERE shi.turma = :turma
            ORDER BY shi.dia, sha.ordem
            """)
    <T> List<T> findResumoByTurma(Turma turma, Class<T> projection);
}
