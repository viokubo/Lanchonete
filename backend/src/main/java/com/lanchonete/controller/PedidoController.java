package com.lanchonete.controller;

import com.lanchonete.dto.ItensDTO;
import com.lanchonete.entities.ItemPedido;
import com.lanchonete.entities.Pedido;
import com.lanchonete.entities.Produto;
import com.lanchonete.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PedidoController {

  private final PedidoService service;

  @Autowired
  public PedidoController(PedidoService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Long> criarPedido() {
    Long pedidoId = service.criarPedido();
    return ResponseEntity.ok(pedidoId);
  }

  @PostMapping("/{pedidoId}/adicionar-produto")
  public ResponseEntity<Void> adicionarProduto(@PathVariable Long pedidoId, @RequestParam Long produtoId, @RequestParam int quantidade) {
    return service.adicionarProduto(pedidoId, produtoId, quantidade);
  }

  @PostMapping("/{pedidoId}/remover-produto")
  public ResponseEntity<Void> removerProduto(@PathVariable Long pedidoId, @RequestParam Long produtoId, @RequestParam int quantidade) {
    return service.removerProduto(pedidoId, produtoId, quantidade);
  }

  @GetMapping("/{pedidoId}/calcular-preco-total")
  public ResponseEntity<BigDecimal> calcularPrecoTotal(@PathVariable Long pedidoId) {
    return service.calcularPrecoTotal(pedidoId);
  }

  @Transactional
  @PostMapping("/{pedidoId}/fechar-pedido")
  public ResponseEntity<String> fecharPedido(@PathVariable Long pedidoId, @RequestParam BigDecimal valorPagamento) {
    return service.fecharPedido(pedidoId, valorPagamento);
  }

  @PostMapping("/{pedidoId}/calcular-preco-total-com-lista")
  public ResponseEntity<BigDecimal> calcularPrecoTotalComLista(@PathVariable Long pedidoId, @RequestBody List<ItensDTO> itensPedido) {
    return service.calcularPrecoTotalComLista(pedidoId, itensPedido);
  }
}
