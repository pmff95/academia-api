package com.example.demo.repository.controle;

import com.example.demo.domain.enums.TipoMatricula;
import com.example.demo.domain.model.controle.ControleMatricula;
import com.example.demo.domain.model.instituicao.Escola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ControleMatriculaRepository extends JpaRepository<ControleMatricula, Integer> {
    Optional<ControleMatricula> findById_AnoAndId_TipoAndId_EscolaId(Integer ano, TipoMatricula tipo, Long escolaId);
}
