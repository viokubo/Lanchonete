package com.lanchonete.services;

import com.lanchonete.dto.ItensDTO;
import com.lanchonete.entities.ItemPedido;
import com.lanchonete.entities.Pedido;
import com.lanchonete.entities.Produto;
import com.lanchonete.repositories.PedidoRepository;
import com.lanchonete.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

  private final ProdutoRepository produtoRepository;
  private final PedidoRepository pedidoRepository;

  @Autowired
  public PedidoService(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
    this.produtoRepository = produtoRepository;
    this.pedidoRepository = pedidoRepository;
  }


  public Long criarPedido() {
    Pedido pedido = new Pedido();
    return pedidoRepository.save(pedido).getId();
  }

  public ResponseEntity<Void> adicionarProduto(Long pedidoId, Long produtoId, int quantidade) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
    Produto produto = produtoRepository.findById(produtoId).orElse(null);
    if (pedido == null || produto == null) {
      return ResponseEntity.notFound().build();
    }

    ItemPedido itemAdicionar = pedido.getItens().stream()
        .filter(itemPedido -> itemPedido.getProduto().getId().equals(produtoId))
        .findFirst()
        .orElse(null);

    if (itemAdicionar != null) {
      itemAdicionar.setQuantidade(quantidade);
    } else {
      ItemPedido novoItem = new ItemPedido(pedido, produto, quantidade);
      pedido.setItens(List.of(novoItem));
    }
    try {
      pedidoRepository.save(pedido);
    }catch (Exception ignored){}

    return ResponseEntity.ok().build();
  }

  @Transactional
  public ResponseEntity<Void> removerProduto(Long pedidoId, Long produtoId, int quantidade) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
    Produto produto = produtoRepository.findById(produtoId).orElse(null);

    if (pedido == null || produto == null) {
      return ResponseEntity.notFound().build();
    }

    List<ItemPedido> items = new LinkedList<>(pedido.getItens());


    ItemPedido itemPedido = items.stream()
        .filter(item -> item.getProduto().getId().equals(produto.getId()))
        .findFirst()
        .orElse(null);

    if (itemPedido == null) {
      return ResponseEntity.badRequest().build();
    }

    if (itemPedido.getQuantidade() <= quantidade) {
      pedidoRepository.deleteItemPedidoById(itemPedido.getId());
    } else {
      itemPedido.setQuantidade(itemPedido.getQuantidade() - quantidade);
      pedidoRepository.save(pedido);
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<BigDecimal> calcularPrecoTotal(Long pedidoId) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

    if (pedido == null) {
      return ResponseEntity.notFound().build();
    }

    BigDecimal precoTotal = pedido.calcularPrecoTotal();
    return ResponseEntity.ok(precoTotal);
  }

  @Transactional
  public ResponseEntity<String> fecharPedido(Long pedidoId, BigDecimal valorPagamento) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

    if (pedido == null) {
      return ResponseEntity.notFound().build();
    }

    BigDecimal precoTotal = pedido.calcularPrecoTotal();

    if (valorPagamento.compareTo(precoTotal) < 0) {
      return ResponseEntity.badRequest().body("Valor de pagamento insuficiente");
    }

    pedidoRepository.delete(pedido);

    return ResponseEntity.ok().build();
  }

  public ResponseEntity<BigDecimal> calcularPrecoTotalComLista(Long pedidoId, List<ItensDTO> itensRequest) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

    if (pedido == null) {
      return ResponseEntity.notFound().build();
    }

    List<Long> produtosId = itensRequest.stream().map(ItensDTO::getProdutoId).collect(Collectors.toList());
    List<Produto> produtos = produtoRepository.findAllById(produtosId);

    BigDecimal precoTotal = pedido.calcularPrecoTotalComLista(itensRequest, produtos);
    return ResponseEntity.ok(precoTotal);
  }
}
