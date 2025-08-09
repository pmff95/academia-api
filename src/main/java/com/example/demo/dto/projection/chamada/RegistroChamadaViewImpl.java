package com.example.demo.dto.projection.chamada;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RegistroChamadaViewImpl(
        UUID uuid,
        LocalDate dataAula,
        Boolean aulaDupla,
        List<ItemChamadaView> presencas,
        Boolean registrado
) implements RegistroChamadaView {

    public RegistroChamadaViewImpl(RegistroChamadaView view, Boolean registrado) {
        this(view != null ? view.getUuid() : null,
                view != null ? view.getDataAula() : null,
                view != null ? view.getAulaDupla() : null,
                view != null ? view.getPresencas() : null,
                registrado);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public LocalDate getDataAula() {
        return dataAula;
    }

    @Override
    public Boolean getAulaDupla() {
        return aulaDupla;
    }

    @Override
    public List<ItemChamadaView> getPresencas() {
        return presencas;
    }

    @Override
    public Boolean getRegistrado() {
        return registrado;
    }
}

