package com.example.demo.dto.projection.dashboard;

import com.example.demo.domain.enums.Perfil;

import java.time.LocalDate;

public interface AniversarianteView {
    String getNome();
    LocalDate getDataNascimento();
    Perfil getPerfil();
    String getFoto();
}
