package com.example.demo.repository;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpfOrEmailOrTelefone(String cpf, String email, String telefone);
    Optional<Usuario> findByUuid(UUID uuid);
    boolean existsByPerfil(Perfil perfil);
}