package com.example.demo.service.controle;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.TipoMatricula;
import com.example.demo.domain.model.controle.ControleMatricula;
import com.example.demo.domain.model.controle.ControleMatriculaId;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.repository.controle.ControleMatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class ControleMatriculaService {
    private final ControleMatriculaRepository repository;

    public String gerarNovaMatricula(Perfil perfil, Escola escola) {
        TipoMatricula tipo = switch (perfil) {
            case ALUNO -> TipoMatricula.ALUNO;
            case PROFESSOR -> TipoMatricula.PROFESSOR;
            case RESPONSAVEL -> TipoMatricula.RESPONSAVEL;
            default -> TipoMatricula.FUNCIONARIO;
        };

        int anoCompleto = Year.now().getValue();     // Ex: 2025
        int ano = anoCompleto % 100;                 // Ex: 25

        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        Long escolaId = usuario.getPerfil() == Perfil.MASTER
                ? escola.getId()
                : usuario.getEscola().getId();

        var id = new ControleMatriculaId();
        id.setAno(anoCompleto);
        id.setTipo(tipo);
        id.setEscolaId(escolaId);

        ControleMatricula controle = repository
                .findById_AnoAndId_TipoAndId_EscolaId(anoCompleto, tipo, escolaId)
                .orElse(null);

        int numero;

        if (controle == null) {
            numero = 1;
            controle = ControleMatricula.builder()
                    .id(id)
                    .numeroAtual(numero)
                    .build();
        } else {
            numero = controle.getNumeroAtual() + 1;
            controle.setNumeroAtual(numero);
        }

        repository.save(controle);

        return String.format("%s%d%d%d", tipo.getSigla(), ano, escolaId, numero);
    }



}
