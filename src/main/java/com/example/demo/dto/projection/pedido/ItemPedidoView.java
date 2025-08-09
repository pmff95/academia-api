package com.example.demo.dto.projection.pedido;

import java.math.BigDecimal;

interface ItemPedidoView {
    String getDescricao();

    Integer getQuantidade();

    BigDecimal getValorUnitario();

    BigDecimal getValorTotal();
}
