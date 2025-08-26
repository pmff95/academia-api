package com.example.demo.service;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.TipoFornecedor;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.FornecedorDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.entity.Fornecedor;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private AlunoPagamentoService alunoPagamentoService;

    private final ModelMapper mapper = new ModelMapper();

    @Test
    void shouldReturnFalseFlagsWhenUserWithoutAcademia() {
        UUID uuid = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setUuid(uuid);
        usuario.setPerfil(Perfil.ADMIN);

        when(usuarioRepository.findByUuid(uuid)).thenReturn(Optional.of(usuario));

        UsuarioService service = new UsuarioService(usuarioRepository, mapper, passwordEncoder, emailService, alunoPagamentoService);

        try (var mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUsuarioLogadoDetalhes)
                    .thenReturn(new UsuarioLogado(uuid, null, Perfil.ADMIN));

            ApiReturn<UsuarioDTO> result = service.buscarUsuarioLogado();

            UsuarioDTO dto = result.getResult();
            assertNotNull(dto.getExibirPatrocinadores());
            assertNotNull(dto.getExibirMarketplace());
            assertFalse(dto.getExibirPatrocinadores());
            assertFalse(dto.getExibirMarketplace());
        }
    }

    @Test
    void shouldReturnNullFlagsForMaster() {
        UUID uuid = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setUuid(uuid);
        usuario.setPerfil(Perfil.MASTER);

        when(usuarioRepository.findByUuid(uuid)).thenReturn(Optional.of(usuario));

        UsuarioService service = new UsuarioService(usuarioRepository, mapper, passwordEncoder, emailService, alunoPagamentoService);

        try (var mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUsuarioLogadoDetalhes)
                    .thenReturn(new UsuarioLogado(uuid, null, Perfil.MASTER));

            ApiReturn<UsuarioDTO> result = service.buscarUsuarioLogado();

            UsuarioDTO dto = result.getResult();
            assertNull(dto.getExibirPatrocinadores());
            assertNull(dto.getExibirMarketplace());
        }
    }

    @Test
    void shouldReturnFornecedorTypeWhenLoggedUserIsFornecedor() {
        UUID uuid = UUID.randomUUID();

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setUuid(uuid);
        fornecedor.setPerfil(Perfil.FORNECEDOR);
        fornecedor.setTipo(TipoFornecedor.SUPLEMENTO);

        when(usuarioRepository.findByUuid(uuid)).thenReturn(Optional.of(fornecedor));

        UsuarioService service = new UsuarioService(usuarioRepository, mapper, passwordEncoder, emailService, alunoPagamentoService);

        try (var mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUsuarioLogadoDetalhes)
                    .thenReturn(new UsuarioLogado(uuid, null, Perfil.FORNECEDOR));

            ApiReturn<UsuarioDTO> result = service.buscarUsuarioLogado();

            UsuarioDTO dto = result.getResult();
            assertTrue(dto instanceof FornecedorDTO);
            assertEquals(TipoFornecedor.SUPLEMENTO, ((FornecedorDTO) dto).getTipo());
        }
    }
}

