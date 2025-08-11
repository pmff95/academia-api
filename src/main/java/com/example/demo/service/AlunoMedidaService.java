package com.example.demo.service;

import com.example.demo.dto.AlunoMedidaDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoMedida;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMedidaMapper;
import com.example.demo.repository.AlunoMedidaRepository;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoMedidaService {

    private final AlunoMedidaRepository repository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoMedidaMapper mapper;

    public AlunoMedidaService(AlunoMedidaRepository repository,
                              AlunoRepository alunoRepository,
                              UsuarioRepository usuarioRepository,
                              AlunoMedidaMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public String adicionarMedida(UUID alunoUuid, AlunoMedidaDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        AlunoMedida medida = mapper.toEntity(dto);
        medida.setAluno(aluno);
        repository.save(medida);
        return "Medida registrada com sucesso";
    }

    public List<AlunoMedidaDTO> listarMedidas(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        return repository.findByAlunoUuid(alunoUuid)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private void validarMesmaAcademia(Aluno aluno) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        boolean isMaster = usuarioLogado != null && usuarioLogado.possuiPerfil(Perfil.MASTER);

        if (usuarioLogado != null && !isMaster) {
            Usuario usuario = usuarioRepository.findByUuid(usuarioLogado.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));

            if (usuario.getAcademia() == null || aluno.getAcademia() == null
                    || !usuario.getAcademia().getUuid().equals(aluno.getAcademia().getUuid())) {
                throw new ApiException("Acesso negado a aluno de outra academia");
            }
        }
    }
}
