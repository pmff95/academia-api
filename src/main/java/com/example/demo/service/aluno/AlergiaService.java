package com.example.demo.service.aluno;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.saude.Alergia;
import com.example.demo.dto.aluno.AlergiaRequest;
import com.example.demo.repository.aluno.AlergiaRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlergiaService {

    private final AlergiaRepository alergiaRepository;
    private final AlunoRepository alunoRepository;

    public AlergiaService(AlergiaRepository alergiaRepository, AlunoRepository alunoRepository) {
        this.alergiaRepository = alergiaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void salvarLista(UUID alunoUuid, List<AlergiaRequest> requests) {
        Aluno aluno = alunoRepository.findByUuid(alunoUuid)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Aluno não encontrado"));

        for (AlergiaRequest req : requests) {
            Alergia alergia = new Alergia();
            alergia.setAluno(aluno);
            alergia.setTipo(req.tipo());
            alergia.setSubstancia(req.substancia());
            alergia.setGravidade(req.gravidade());
            alergia.setObservacoes(req.observacoes());
            alergia.setDataDiagnostico(req.dataDiagnostico());
            alergia.setCuidadosEmergenciais(req.cuidadosEmergenciais());

            alergiaRepository.save(alergia);
        }
    }

    @Transactional
    public void substituirLista(UUID alunoUuid, List<AlergiaRequest> requests) {
        List<Alergia> existentes = alergiaRepository.findByAluno_Uuid(alunoUuid);
        if (!existentes.isEmpty()) {
            alergiaRepository.deleteAll(existentes);
        }
        if (requests != null && !requests.isEmpty()) {
            salvarLista(alunoUuid, requests);
        }
    }

    public List<Alergia> listarPorAluno(UUID alunoUuid) {
        return alergiaRepository.findByAluno_Uuid(alunoUuid);
    }
}
