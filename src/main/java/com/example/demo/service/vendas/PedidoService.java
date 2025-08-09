package com.example.demo.service.vendas;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.domain.enums.StatusPedido;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.vendas.ItemPedido;
import com.example.demo.domain.model.vendas.Pedido;
import com.example.demo.domain.model.vendas.Produto;
import com.example.demo.dto.projection.pedido.PedidoView;
import com.example.demo.dto.vendas.PedidoRequest;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.specification.PedidoSpecification;
import com.example.demo.repository.vendas.PedidoRepository;
import com.example.demo.repository.vendas.ProdutoRepository;
import com.example.demo.service.usuario.UsuarioService;
import com.example.demo.service.carteira.CarteiraService;
import com.example.demo.service.vendas.PedidoAssembler;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioService usuarioService;
    private final CarteiraService carteiraService;
    private final PedidoAssembler pedidoAssembler;

    public PedidoService(
            PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
            AlunoRepository alunoRepository, UsuarioService usuarioService,
            CarteiraService carteiraService, PedidoAssembler pedidoAssembler
    ) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioService = usuarioService;
        this.carteiraService = carteiraService;
        this.pedidoAssembler = pedidoAssembler;
    }

    /* ───────────────────────── CRIAR ───────────────────────── */

    @Transactional
    public void salvar(PedidoRequest request) {

        Pedido pedido = pedidoAssembler.montarPedido(request, new Pedido());

        pedidoRepository.save(pedido);
    }

    /* ───────────────────────── ATUALIZAR ───────────────────────── */

    @Transactional
    public void salvar(UUID uuid, PedidoRequest request) {

        Pedido pedido = pedidoRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Pedido não encontrado."));

        pedido.getItens().clear();
        pedidoRepository.flush();

        pedidoAssembler.montarPedido(request, pedido);

        pedidoRepository.save(pedido);
    }

    /* ───────────────────────── BUSCAR ───────────────────────── */

    public PedidoView buscarPorUuid(UUID uuid) {
        return pedidoRepository.findByUuid(uuid, PedidoView.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Pedido não encontrado."));
    }

    /* ───────────────────────── LISTAR ───────────────────────── */

    public Page<PedidoView> listar(PedidoSpecification specification, Pageable pageable) {
        Page<PedidoView> page =
                pedidoRepository.findAllProjected(specification, pageable, PedidoView.class);

        if (page.isEmpty()) {
            throw EurekaException.ofNoContent("Consulta com filtro informado não possui dados para retorno");
        }
        return page;
    }

    /* ───────────────────────── STATUS ───────────────────────── */

    @Transactional
    public void cancelar(UUID uuid) {
        alterarStatus(uuid, StatusPedido.CANCELADO);
    }

    @Transactional
    public void confirmar(UUID uuid) {

        Pedido pedido = pedidoRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Pedido não encontrado."));

        if (pedido.getStatusPedido() == StatusPedido.CONCLUIDO) {
            throw EurekaException.ofValidation("Pedido já está concluído.");
        }

        pedido.setStatusPedido(StatusPedido.CONCLUIDO);
        pedidoRepository.save(pedido);

        carteiraService.debitarCompra(
                pedido.getComprador().getUuid(),
                pedido.getTotal(),
                pedido,
                pedido.getVendedor()
        );
    }
    private void alterarStatus(UUID uuid, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Pedido não encontrado."));

        pedido.setStatusPedido(novoStatus);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void comprarAgora(PedidoRequest request) {
        Aluno aluno = alunoRepository.findByUuid(request.compradorId())
                .orElseThrow(() -> EurekaException.ofNotFound("Aluno não encontrado."));

        Usuario vendedor = usuarioService.findByUuid(SecurityUtils.getUsuarioLogado().getUuid());

        BigDecimal total = request.itens().stream()
                .map(item -> item.valorUnitario().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        Pedido pedido = pedidoAssembler.montarPedido(request, new Pedido());
        pedido.setStatusPedido(StatusPedido.CONCLUIDO);
        pedidoRepository.save(pedido);

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidadeVendida(produto.getQuantidadeVendida() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        carteiraService.debitarCompra(
                aluno.getUuid(),
                total,
                pedido,
                vendedor
        );
    }

}
