package com.lucas.ecommerce;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(
        @NotNull Long clienteId,
        @NotEmpty @Valid List<ItemRequest> itens
) {
    public record ItemRequest(
            @NotNull Long produtoId,
            @NotNull @Min(1) Integer quantidade
    ) {}
}