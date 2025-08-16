package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FichaTreinoDTO {
    private UUID uuid;
    private UUID alunoUuid;
    private UUID professorUuid;
    private boolean preset;
    private List<FichaTreinoCategoriaDTO> categorias;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getAlunoUuid() {
        return alunoUuid;
    }

    public void setAlunoUuid(UUID alunoUuid) {
        this.alunoUuid = alunoUuid;
    }

    public UUID getProfessorUuid() {
        return professorUuid;
    }

    public void setProfessorUuid(UUID professorUuid) {
        this.professorUuid = professorUuid;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public List<FichaTreinoCategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<FichaTreinoCategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
