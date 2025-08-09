package com.example.demo.service.vendas;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.StatusPedido;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.vendas.ItemPedido;
import com.example.demo.domain.model.vendas.Pedido;
import com.example.demo.domain.model.vendas.Produto;
import com.example.demo.dto.vendas.ItemPedidoRequest;
import com.example.demo.dto.vendas.PedidoRequest;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.vendas.ProdutoRepository;
import com.example.demo.service.usuario.UsuarioService;
import org.springframework.stereotype.Component;

/**
 * Classe auxiliar responsável por montar a entidade {@link Pedido} a partir
 * dos dados recebidos via {@link PedidoRequest}.
 */
@Component
public class PedidoAssembler {

    private final ProdutoRepository produtoRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioService usuarioService;

    public PedidoAssembler(ProdutoRepository produtoRepository,
                           AlunoRepository alunoRepository,
                           UsuarioService usuarioService) {
        this.produtoRepository = produtoRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Preenche o objeto {@link Pedido} (novo ou existente) a partir do DTO.
     * Retorna a própria instância para encadear persistência.
     */
    public Pedido montarPedido(PedidoRequest dto, Pedido pedido) {

        UsuarioLogado vendedorLogado = SecurityUtils.getUsuarioLogado();
        Escola escola = vendedorLogado.getEscola();

        Aluno comprador = alunoRepository.findByUuid(dto.compradorId())
                .orElseThrow(() -> EurekaException.ofNotFound("Comprador não encontrado."));

        Usuario vendedor = usuarioService.findByUuid(vendedorLogado.getUuid());

        pedido.setVendedor(vendedor);
        pedido.setEscola(escola);
        pedido.setComprador(comprador);
        pedido.setStatusPedido(StatusPedido.ABERTO);

        for (ItemPedidoRequest itemDto : dto.itens()) {

            Produto produto = produtoRepository.findByUuid(itemDto.produtoId())
                    .orElseThrow(() -> EurekaException.ofNotFound("Produto não encontrado."));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setDescricao(produto.getNome());
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());

            pedido.addItem(item);
        }

        return pedido;
    }
}

