package com.example.demo.service.instituicao;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.dto.instituicao.EscolaCreationRequest;
import com.example.demo.dto.instituicao.EscolaRequest;
import com.example.demo.dto.instituicao.EscolaEnderecoRequest;
import com.example.demo.dto.instituicao.EscolaEnderecoResponse;
import com.example.demo.dto.instituicao.EscolaFinanceiroResponse;
import com.example.demo.dto.instituicao.EscolaFullResponse;
import com.example.demo.dto.projection.escola.EscolaIdAndName;
import com.example.demo.dto.projection.escola.EscolaView;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.repository.instituicao.EscolaRepository;
import com.example.demo.repository.specification.EscolaSpecification;
import com.example.demo.service.controle.ControleMatriculaService;
import com.example.demo.service.usuario.UsuarioService;
import com.example.demo.service.instituicao.EscolaEnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;
    private final UsuarioService usuarioService;
    private final EscolaEnderecoService escolaEnderecoService;
    private final EscolaFinanceiroService escolaFinanceiroService;

    @Transactional
    public UUID salvar(EscolaCreationRequest request) {

        String nome = request.nome();
        String nomeCurto = request.nomeCurto();
        String cnpj = request.cnpj();

        Escola escola = escolaRepository.findByCnpj(
                cnpj).orElse(null);

        if (Objects.nonNull(escola))
            throw EurekaException.ofValidation("CNPJ já cadastrado.");

        escola = new Escola();
        escola.setNome(nome);
        escola.setNomeCurto(nomeCurto);
        escola.setCnpj(cnpj);
        escola.setStatus(Status.ATIVO);
        escolaRepository.save(escola);

        if (request.endereco() != null) {
            escolaEnderecoService.save(escola.getUuid(), request.endereco());
        }

        if (request.financeiro() != null) {
            escolaFinanceiroService.save(escola.getUuid(), request.financeiro());
        }

        usuarioService.createUser(
                new UsuarioRequest(
                        null,
                        escola.getUuid(),
                        request.nomeAdmin(),
                        null,
                        request.emailAdmin(),
                        request.matriculaAdmin(),
                        request.cpfAdmin(),
                        request.telefoneAdmin(),
                        Perfil.ADMIN
                )
        );

        return escola.getUuid();

    }

    public void salvar(UUID uuid, EscolaRequest request) {

        String cnpj = request.cnpj();
        String nome = request.nome();
        String nomeCurto = request.nomeCurto();

        Escola escola = escolaRepository.findByCnpj(
                cnpj).orElse(null);

        if (Objects.nonNull(escola) && !uuid.equals(escola.getUuid()))
            throw EurekaException.ofConflict("CNPJ já cadastrado.");

        escola.setNome(nome);
        escola.setNomeCurto(nomeCurto);
        escola.setCnpj(cnpj);

        escolaRepository.save(escola);

        EscolaEnderecoRequest enderecoRequest = request.endereco();
        if (enderecoRequest != null) {
            escolaEnderecoService.save(uuid, enderecoRequest);
        }
    }

    public EscolaFullResponse buscarPorUuid(UUID uuid) {
        Escola escola = this.findByUuid(uuid);

        EscolaEnderecoResponse endereco = null;
        if (escolaEnderecoService.existsByEscola(uuid)) {
            var entity = escolaEnderecoService.findByEscola(uuid);
            endereco = new EscolaEnderecoResponse(
                    entity.getCep(),
                    entity.getEndereco(),
                    entity.getNumero(),
                    entity.getBairro(),
                    entity.getComplemento(),
                    entity.getCidade(),
                    entity.getEstado()
            );
        }

        EscolaFinanceiroResponse financeiro = null;
        try {
            var fin = escolaFinanceiroService.findByEscolaId(uuid);
            financeiro = new EscolaFinanceiroResponse(
                    fin.getDiaPagamento(),
                    fin.getDiaRecebimento()
            );
        } catch (EurekaException ignored) {
        }

        UsuarioFull admin = usuarioService.findByEscolaIdAndPerfil(uuid, Perfil.ADMIN).orElse(null);

        return new EscolaFullResponse(
                escola.getUuid(),
                escola.getNome(),
                escola.getNomeCurto(),
                escola.getCnpj(),
                escola.getStatus(),
                escola.getCriadoEm(),
                endereco,
                financeiro,
                admin
        );
    }

    public Page<EscolaView> listar(EscolaSpecification specification, Pageable pageable) {
        Page<EscolaView> page = escolaRepository.findAllProjected(specification, pageable, EscolaView.class);

        if (page.isEmpty()) {
            throw EurekaException.ofNoContent("Consulta com filtro informado não possui dados para retorno");
        }

        return page;
    }

    public void inativar(UUID uuid) {

        Escola escola = findByUuid(uuid);

        escola.setStatus(Status.INATIVO);

        escolaRepository.save(escola);
    }

    public void ativar(UUID uuid) {

        Escola escola = findByUuid(uuid);

        escola.setStatus(Status.ATIVO);

        escolaRepository.save(escola);
    }

    public Escola findByUuid(UUID uuid) {
        return escolaRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));
    }

    public List<EscolaIdAndName> getCombobox() {
        return escolaRepository.findAllProjected();
    }
}
