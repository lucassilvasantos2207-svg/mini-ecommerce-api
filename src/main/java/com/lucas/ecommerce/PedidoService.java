package com.lucas.ecommerce;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoService(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @Transactional
    public Pedido criar(PedidoRequest request) {
        Cliente cliente = clientes.findById(request.clienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Pedido pedido = new Pedido(cliente);

        for (PedidoRequest.ItemRequest item : request.itens()) {
            Produto produto = produtos.findById(item.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Produto não encontrado: " + item.produtoId()));

            if (produto.getEstoque() < item.quantidade()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Estoque insuficiente para o produto " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - item.quantidade());
            pedido.adicionarItem(new ItemPedido(pedido, produto, item.quantidade()));
        }

        return pedidos.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    public Pedido buscar(Long id) {
        return pedidos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
}