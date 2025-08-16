package com.example.demo.service;

import com.example.demo.dto.AlunoObservacaoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoObservacao;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoObservacaoMapper;
import com.example.demo.repository.AlunoObservacaoRepository;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoObservacaoService {

    private final AlunoObservacaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoObservacaoMapper mapper;

    public AlunoObservacaoService(AlunoObservacaoRepository repository,
                                  AlunoRepository alunoRepository,
                                  UsuarioRepository usuarioRepository,
                                  AlunoObservacaoMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public String adicionarObservacao(UUID alunoUuid, AlunoObservacaoDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        validarMesmaAcademia(aluno);
        AlunoObservacao obs = mapper.toEntity(dto);
        obs.setAluno(aluno);
        repository.save(obs);
        return "Observação registrada com sucesso";
    }

    public List<AlunoObservacaoDTO> listarObservacoes(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        validarMesmaAcademia(aluno);
        return repository.findByAlunoUuid(alunoUuid)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private void validarMesmaAcademia(Aluno aluno) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
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
