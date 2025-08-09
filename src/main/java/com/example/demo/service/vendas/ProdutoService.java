package com.example.demo.service.vendas;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.vendas.Produto;
import com.example.demo.dto.projection.ProdutoView;
import com.example.demo.dto.vendas.ProdutoRequest;
import com.example.demo.repository.specification.ProdutoSpecification;
import com.example.demo.repository.vendas.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private final CategoriaService categoriaService;
    private final ProdutoRepository repository;

    public ProdutoService(CategoriaService categoriaService, ProdutoRepository repository) {
        this.categoriaService = categoriaService;
        this.repository = repository;
    }

    public void salvar(ProdutoRequest request) {
        Produto produto = new Produto();

        produto.setNome(request.nome());
        produto.setFoto(request.foto());
        produto.setPreco(request.preco());
        produto.setDepartamento(request.departamento());
        produto.setCategoria(categoriaService.buscarPorUuid(request.categoriaId()));
        produto.setStatus(Status.ATIVO);
        produto.setQuantidadeVendida(0L);

        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        produto.setEscola(usuarioLogado.getEscola());

        repository.save(produto);
    }

    public void salvar(UUID uuid, ProdutoRequest request) {
        Produto produto = new Produto();

        if (uuid != null) {
            produto = repository.findByUuid(uuid)
                    .orElseThrow(() -> EurekaException.ofNotFound("Produto não encontrado."));
        }

        produto.setNome(request.nome());
        produto.setFoto(request.foto());
        produto.setPreco(request.preco());
        produto.setDepartamento(request.departamento());
        produto.setCategoria(categoriaService.buscarPorUuid(request.categoriaId()));

        repository.save(produto);
    }

    public ProdutoRequest buscarPorUuid(UUID uuid) {
        return repository.BuscarProdutoPorUuid(uuid);
    }

    public Page<ProdutoView> listar(ProdutoSpecification specification, Pageable pageable) {
        Page<ProdutoView> page = repository.findAllProjected(specification, pageable, ProdutoView.class);

        if (page.isEmpty()) {
            throw EurekaException.ofNoContent("Consulta com filtro informado não possui dados para retorno");
        }

        return page;
    }

    public void modificarStatus(UUID uuid) {
        Optional<Produto> produto = repository.findByUuid(uuid);
        produto.get().setStatus(produto.get().getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO);
        repository.save(produto.get());
    }

}
