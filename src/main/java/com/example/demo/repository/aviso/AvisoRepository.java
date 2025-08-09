package com.example.demo.repository.aviso;

import com.example.demo.domain.model.aviso.Aviso;
import com.example.demo.dto.projection.aviso.AvisoView;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvisoRepository extends BaseRepository<Aviso, Long> {

    Optional<Aviso> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    @Query("""
            SELECT a.uuid AS uuid,
                   a.titulo AS titulo,
                   a.mensagem AS mensagem,
                   a.criadoEm AS criadoEm,
                   u.nome AS usuarioNome,
                   u.perfil AS usuarioPerfil
            FROM Aviso a
            JOIN a.usuario u
            ORDER BY a.criadoEm DESC
            """)
    List<AvisoView> findAllView();
}
