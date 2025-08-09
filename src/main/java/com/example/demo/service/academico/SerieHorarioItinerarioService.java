package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.academico.SerieHorarioItinerario;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.dto.academico.HorarioItinerarioRequest;
import com.example.demo.dto.academico.SerieHorarioItinerarioCadastroRequest;
import com.example.demo.repository.academico.SerieHorarioAulaRepository;
import com.example.demo.repository.academico.SerieHorarioItinerarioRepository;
import com.example.demo.repository.academico.TurmaRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SerieHorarioItinerarioService {

    private final SerieHorarioItinerarioRepository repository;
    private final SerieHorarioAulaRepository serieHorarioAulaRepository;
    private final TurmaRepository turmaRepository;

    public SerieHorarioItinerarioService(SerieHorarioItinerarioRepository repository,
                                         SerieHorarioAulaRepository serieHorarioAulaRepository,
                                         TurmaRepository turmaRepository) {
        this.repository = repository;
        this.serieHorarioAulaRepository = serieHorarioAulaRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public void salvarHorarios(SerieHorarioItinerarioCadastroRequest request) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        for (UUID turmaUuid : request.turmas()) {
            Turma turma = turmaRepository.findByUuid(turmaUuid)
                    .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));
            Serie serie = turma.getSerie();
            validarSerie(serie);
            repository.deleteAll(repository.findByTurma(turma));
            repository.flush();

            Set<String> chaves = new HashSet<>();
            List<SerieHorarioItinerario> novos = new ArrayList<>();

            for (HorarioItinerarioRequest h : request.horarios()) {
                String chave = h.dia() + "-" + h.ordem();
                if (chaves.add(chave)) {
                    serieHorarioAulaRepository.findBySerieAndOrdem(serie, h.ordem())
                            .orElseThrow(() -> EurekaException.ofNotFound("Horário padrão não encontrado."));
                    var shi = new SerieHorarioItinerario();
                    shi.setTurma(turma);
                    shi.setEscola(usuarioLogado.getEscola());
                    shi.setDia(h.dia());
                    shi.setOrdem(h.ordem());
                    novos.add(shi);
                }
            }
            repository.saveAll(novos);
        }
    }


    private void validarSerie(Serie serie) {
        if (serie != Serie.PRIMEIRO_MEDIO && serie != Serie.SEGUNDO_MEDIO) {
            throw EurekaException.ofValidation("Somente 1º e 2º ano do ensino médio podem possuir itinerário.");
        }
    }

    public <T> List<T> listar(UUID turmaUuid, Class<T> projection) {
        Turma turma = turmaRepository.findByUuid(turmaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));
        Serie serie = turma.getSerie();
        validarSerie(serie);
        return repository.findResumoByTurma(turma, projection);
    }
}
