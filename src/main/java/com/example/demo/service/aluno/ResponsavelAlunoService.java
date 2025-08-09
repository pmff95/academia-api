package com.example.demo.service.aluno;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.GrauParentesco;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.aluno.ResponsavelAluno;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.aluno.ResponsavelAlunoRequest;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.aluno.ResponsavelAlunoRepository;
import com.example.demo.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ResponsavelAlunoService {

    private final ResponsavelAlunoRepository responsavelAlunoRepository;
    private final UsuarioService usuarioService;
    private final AlunoRepository alunoRepository;

    public ResponsavelAlunoService(ResponsavelAlunoRepository responsavelAlunoRepository,
                                   UsuarioService usuarioService, AlunoRepository alunoRepository) {
        this.usuarioService = usuarioService;
        this.responsavelAlunoRepository = responsavelAlunoRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public List<Long> create(ResponsavelAlunoRequest request, UUID alunoId) {

        validarDuplicidadeDosResponsaveis(request);

        Long responsavel1 = criaResponsavel(request.grauParentesco(), alunoId, request.responsavel());
        return Stream.of(
                        responsavel1,
                        (request.grauParentescoResponsavel2() != null && request.responsavel2() != null)
                                ? criaResponsavel(request.grauParentescoResponsavel2(), alunoId, request.responsavel2())
                                : null
                )
                .filter(Objects::nonNull)
                .toList();
    }

    private Long criaResponsavel(GrauParentesco grauParentesco, UUID alunoId, UsuarioRequest responsavelRequest) {
        if (Objects.isNull(grauParentesco))
            throw EurekaException.ofValidation("Grau de parentesco é obrigatório.");

        if (Objects.isNull(responsavelRequest.perfil()))
            throw EurekaException.ofValidation("Perfil é obrigatório.");

        if (!Objects.equals(responsavelRequest.perfil(), Perfil.RESPONSAVEL))
            throw EurekaException.ofValidation("O usuário deve ser do tipo Responsável.");

        Aluno aluno = this.alunoRepository
                .findByUuid(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        validarDadosDiferentesDoAluno(aluno, responsavelRequest);

        Usuario responsavel;
        if (responsavelRequest.uuid() != null) {
            this.usuarioService.updateUser(responsavelRequest.uuid(), responsavelRequest);
            responsavel = this.usuarioService.findByUuid(responsavelRequest.uuid());
        } else {
            UUID responsavelId = this.usuarioService.createUser(responsavelRequest);
            responsavel = this.usuarioService.findByUuid(responsavelId);
        }

        Optional<ResponsavelAluno> existente = this.responsavelAlunoRepository
                .findByResponsavel_IdAndAluno_Id(responsavel.getId(), aluno.getId());

        ResponsavelAluno responsavelAluno = existente.orElseGet(ResponsavelAluno::new);
        responsavelAluno.setResponsavel(responsavel);
        responsavelAluno.setAluno(aluno);
        responsavelAluno.setGrauParentesco(grauParentesco);

        this.responsavelAlunoRepository.save(responsavelAluno);

        return responsavelAluno.getId();
    }

    @Transactional
    public void substituirResponsaveis(UUID alunoUuid, ResponsavelAlunoRequest request) {
        if (request == null) {
            return;
        }

        List<Long> atualizados = create(request, alunoUuid);

        List<ResponsavelAluno> existentes = responsavelAlunoRepository.findByAluno_Uuid(alunoUuid);
        existentes.stream()
                .filter(r -> !atualizados.contains(r.getId()))
                .forEach(responsavelAlunoRepository::delete);
    }

    private void validarDuplicidadeDosResponsaveis(ResponsavelAlunoRequest request) {
        if (request == null || request.responsavel2() == null) {
            return;
        }

        UsuarioRequest r1 = request.responsavel();
        UsuarioRequest r2 = request.responsavel2();

        String email1 = r1.email() != null ? r1.email().trim() : null;
        String email2 = r2.email() != null ? r2.email().trim() : null;
        if (email1 != null && email2 != null && email1.equalsIgnoreCase(email2)) {
            throw EurekaException.ofValidation("Emails dos responsáveis não podem ser iguais.");
        }

        String cpf1 = r1.cpf() != null ? r1.cpf().trim().replaceAll("\\D", "") : null;
        String cpf2 = r2.cpf() != null ? r2.cpf().trim().replaceAll("\\D", "") : null;
        if (cpf1 != null && cpf2 != null && cpf1.equals(cpf2)) {
            throw EurekaException.ofValidation("CPFs dos responsáveis não podem ser iguais.");
        }
    }

    private void validarDadosDiferentesDoAluno(Aluno aluno, UsuarioRequest responsavelRequest) {
        String emailResponsavel = responsavelRequest.email() != null ? responsavelRequest.email().trim() : null;
        String cpfResponsavel = responsavelRequest.cpf() != null ? responsavelRequest.cpf().trim().replaceAll("\\D", "") : null;

        String emailAluno = aluno.getEmail();
        String cpfAluno = aluno.getCpf() != null ? aluno.getCpf().replaceAll("\\D", "") : null;

        if (emailResponsavel != null && emailAluno != null && emailResponsavel.equalsIgnoreCase(emailAluno)) {
            throw EurekaException.ofValidation("Email do responsável não pode ser igual ao email do aluno.");
        }

        if (cpfResponsavel != null && cpfAluno != null && cpfResponsavel.equals(cpfAluno)) {
            throw EurekaException.ofValidation("CPF do responsável não pode ser igual ao CPF do aluno.");
        }
    }

}
