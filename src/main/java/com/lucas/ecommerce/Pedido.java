package com.lucas.ecommerce;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    private LocalDateTime data = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;

    protected Pedido() {
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        total = total.add(item.getSubtotal());
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getData() { return data; }
    public StatusPedido getStatus() { return status; }
    public List<ItemPedido> getItens() { return itens; }
    public BigDecimal getTotal() { return total; }
}