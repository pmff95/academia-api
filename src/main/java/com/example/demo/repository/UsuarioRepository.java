package com.example.demo.repository;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpfOrEmailOrTelefoneOrNick(String cpf, String email, String telefone, String nick);

    Optional<Usuario> findByUuid(UUID uuid);
//    @Query(value = "select * from public.usuario where uuid = :uuid", nativeQuery = true)
//    Optional<Usuario> findNativeByUuid(@Param("uuid") UUID uuid);

    boolean existsByPerfil(Perfil perfil);
}
