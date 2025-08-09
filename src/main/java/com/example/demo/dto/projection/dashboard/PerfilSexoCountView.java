package com.example.demo.dto.projection.dashboard;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Sexo;

public interface PerfilSexoCountView {
    Perfil getPerfil();
    Sexo getSexo();
    Long getTotal();
}