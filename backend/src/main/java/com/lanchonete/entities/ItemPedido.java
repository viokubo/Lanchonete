package com.lanchonete.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "item_pedido")
@Table(name = "item_pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne
  @JoinColumn(name = "produto_id")
  Produto produto;

  @ManyToOne
  @JoinColumn(name = "pedido_id")
  Pedido pedido;

  @Column
  int quantidade;

  public ItemPedido(Pedido pedido, Produto produto, int quantidade) {
    this.pedido = pedido;
    this.produto = produto;
    this.quantidade = quantidade;
  }
}



