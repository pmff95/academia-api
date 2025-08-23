package com.example.demo.dto;

import java.util.UUID;

public class PatrocinioDTO {
    private UUID uuid;
    private UUID patrocinadorUuid;
    private String titulo;
    private String descricao;
    private String link;
    private String imagemUrl;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getPatrocinadorUuid() {
        return patrocinadorUuid;
    }

    public void setPatrocinadorUuid(UUID patrocinadorUuid) {
        this.patrocinadorUuid = patrocinadorUuid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}

