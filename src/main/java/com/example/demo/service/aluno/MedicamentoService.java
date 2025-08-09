package com.example.demo.service.aluno;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.saude.Medicamento;
import com.example.demo.dto.aluno.MedicamentoRequest;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.aluno.MedicamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final AlunoRepository alunoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository, AlunoRepository alunoRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void salvarLista(UUID alunoUuid, List<MedicamentoRequest> requests) {
        Aluno aluno = alunoRepository.findByUuid(alunoUuid)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Aluno não encontrado"));

        for (MedicamentoRequest req : requests) {
            Medicamento medicamento = new Medicamento();
            medicamento.setAluno(aluno);
            medicamento.setNome(req.nome());
            medicamento.setDosagem(req.dosagem());
            medicamento.setFrequencia(req.frequencia());
            medicamento.setObservacoes(req.observacoes());

            medicamentoRepository.save(medicamento);
        }
    }

    @Transactional
    public void substituirLista(UUID alunoUuid, List<MedicamentoRequest> requests) {
        List<Medicamento> existentes = medicamentoRepository.findByAluno_Uuid(alunoUuid);
        if (!existentes.isEmpty()) {
            medicamentoRepository.deleteAll(existentes);
        }
        if (requests != null && !requests.isEmpty()) {
            salvarLista(alunoUuid, requests);
        }
    }

    public List<Medicamento> listarPorAluno(UUID alunoUuid) {
        return medicamentoRepository.findByAluno_Uuid(alunoUuid);
    }
}
