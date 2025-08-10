package com.example.demo.repository;

import com.example.demo.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> findByCpfOrEmailOrTelefone(String cpf, String email, String telefone);
}
