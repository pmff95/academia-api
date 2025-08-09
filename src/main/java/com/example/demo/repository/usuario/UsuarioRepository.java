package com.example.demo.repository.usuario;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.projection.dashboard.AniversarianteView;
import com.example.demo.dto.projection.dashboard.PerfilSexoCountView;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.usuario.UsuarioView;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projectionClass);

    /* Utilizado para o login */
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.escola WHERE u.matricula = :matricula and u.status = 'ATIVO'")
    Optional<Usuario> buscarUsuarioAtivoComEscolaPorMatricula(@Param("matricula") String matricula);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.escola " +
            "WHERE (" +
            "   u.email = :login " +
            "   OR u.matricula = :login " +
            "   OR u.matriculaManual = :login " +
            "   OR u.nickname = :login " +
            "   OR u.cpf = :login " +
            "   OR u.telefone = :login" +
            ") and u.status = 'ATIVO'")
    Optional<Usuario> buscarUsuarioAtivoComEscolaPorLogin(@Param("login") String login);


    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.escola WHERE u.uuid = :uuid")
    Optional<Usuario> findByUuid(@Param("uuid") UUID uuid);

    boolean existsByEmailAndEscolaUuid(String email, UUID escolaUuid);

    boolean existsByEmailAndEscolaUuidAndUuidNot(String email, UUID escolaUuid, UUID uuid);

    boolean existsByCpfAndEscolaUuid(String cpf, UUID escolaUuid);

    boolean existsByCpfAndEscolaUuidAndUuidNot(String cpf, UUID escolaUuid, UUID uuid);

    boolean existsByTelefone(String telefone);

    boolean existsByTelefoneAndUuidNot(String telefone, UUID uuid);

    Page<UsuarioView> findAllByEscolaUuid(UUID escolaId, Pageable pageable);

    @Query("""
                SELECT u FROM Usuario u
                WHERE u.escola.uuid = :escolaId
                AND u.perfil = :perfil
            """)
    Optional<UsuarioFull> findByEscolaIdAndPerfil(@Param("escolaId") UUID escolaId, @Param("perfil") Perfil perfil);

    @Query("""
            
            SELECT u.nome AS nome, u.dataNascimento AS dataNascimento, u.perfil AS perfil, u.foto AS foto
                FROM Usuario u
                WHERE u.escola.uuid = :escolaId
                  AND u.status = 'ATIVO'
                  AND MONTH(u.dataNascimento) = :mes
                ORDER BY u.dataNascimento

            """)
    List<AniversarianteView> findAniversariantesByMes(@Param("escolaId") UUID escolaId, @Param("mes") int mes);

    @Query("""
            SELECT u.perfil AS perfil, u.sexo AS sexo, COUNT(u) AS total
            FROM Usuario u
            WHERE u.escola.uuid = :escolaId
              AND u.status = 'ATIVO'
              AND u.perfil IN ('ALUNO','PROFESSOR')
            GROUP BY u.perfil, u.sexo
            """)
    List<PerfilSexoCountView> countByPerfilAndSexo(@Param("escolaId") UUID escolaId);

    long countByEscolaUuidAndStatus(UUID escolaId, Status status);


}
