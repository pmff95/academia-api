package com.example.demo.service.aviso;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.aviso.Aviso;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.aviso.AvisoRequest;
import com.example.demo.dto.projection.aviso.AvisoView;
import com.example.demo.repository.aviso.AvisoRepository;
import com.example.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvisoService {

    private final AvisoRepository avisoRepository;
    private final UsuarioRepository usuarioRepository;

    public AvisoService(AvisoRepository avisoRepository, UsuarioRepository usuarioRepository) {
        this.avisoRepository = avisoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void criar(AvisoRequest request) {
        UsuarioLogado logado = SecurityUtils.getUsuarioLogado();
        Usuario usuario = usuarioRepository.findById(logado.getId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Usuário não encontrado"));

        Aviso aviso = new Aviso();
        aviso.setTitulo(request.titulo());
        aviso.setMensagem(request.mensagem());
        aviso.setUsuario(usuario);

        avisoRepository.save(aviso);
    }

    @Transactional
    public void atualizar(UUID uuid, AvisoRequest request) {
        Aviso aviso = avisoRepository.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Aviso não encontrado"));

        aviso.setTitulo(request.titulo());
        aviso.setMensagem(request.mensagem());

        avisoRepository.save(aviso);
    }

    public List<AvisoView> listar() {
        return avisoRepository.findAllView();
    }
}
