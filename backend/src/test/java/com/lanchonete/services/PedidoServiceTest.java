package com.lanchonete.services;

import com.lanchonete.entities.ItemPedido;
import com.lanchonete.entities.Pedido;
import com.lanchonete.entities.Produto;
import com.lanchonete.repositories.PedidoRepository;
import com.lanchonete.repositories.ProdutoRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singleton;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoServiceTest extends TestCase {
  @Mock
  private ProdutoRepository produtoRepository;
  @Mock
  private PedidoRepository pedidoRepository;
  @InjectMocks
  private PedidoService pedidoService;
  private Pedido pedido;
  private Produto produto;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    pedido = new Pedido();
    produto = getProduto();
  }

  public void testCriarPedido() {
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

    Long result = pedidoService.criarPedido();

    assertEquals(1L, result.longValue());
    verify(pedidoRepository, times(1)).save(any(Pedido.class));
  }

  public void testAdicionarProduto() {
    Pedido pedido = new Pedido();
    Produto produto = new Produto();
    pedido.setId(1L);
    pedido.setItens(List.of());
    produto.setId(1L);

    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.adicionarProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.ok().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, times(1)).save(any(Pedido.class));
  }

  public void testAdicionarProdutoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

    ResponseEntity<Void> result = pedidoService.adicionarProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
  }

  public void testAdicionarPedidoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.adicionarProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
  }

  public void testAumentarQuantidadeDeProdutosNoPedido() {
    Pedido pedido = new Pedido();
    Produto produto = new Produto();
    pedido.setId(1L);
    pedido.setItens(List.of(new ItemPedido(pedido, produto, 3)));
    produto.setId(1L);

    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.adicionarProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.ok().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, times(1)).save(any(Pedido.class));
  }

  public void testRemoverProduto() {
    Pedido pedido = new Pedido();
    Produto produto = new Produto();
    ItemPedido itemPedido = new ItemPedido();
    pedido.setId(1L);
    produto.setId(1L);
    itemPedido.setId(1L);
    itemPedido.setProduto(produto);
    itemPedido.setQuantidade(2);
    pedido.setItens(List.of(itemPedido));
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.removerProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.ok().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, times(1)).deleteItemPedidoById(anyLong());
    verify(pedidoRepository, never()).save(any(Pedido.class));
  }

  public void testRemoverPedidoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.removerProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
  }

  public void testRemoverProdutoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

    ResponseEntity<Void> result = pedidoService.removerProduto(1L, 1L, 2);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
  }

  public void testRemoverProdutoForaDoPedido() {
    pedido.setItens(List.of(new ItemPedido(null, new Produto(1154L, "Bauru", BigDecimal.valueOf(2.50)),3)));
    produto.setId(12L);
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.removerProduto(1L, 1L, 1);

    assertEquals(ResponseEntity.badRequest().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
  }

  public void testDiminuirQuantidadeDeProduto() {
    produto.setId(12L);
    pedido.setItens(List.of(new ItemPedido(pedido, produto, 10)));
    pedido.setId(1L);
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(produto));

    ResponseEntity<Void> result = pedidoService.removerProduto(1L, 12L, 5);

    assertEquals(ResponseEntity.ok().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, never()).deleteItemPedidoById(anyLong());
    verify(pedidoRepository, times(1)).save(any(Pedido.class));
  }

  public void testCalcularPrecoTotal() {
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setItens(List.of());
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));

    ResponseEntity<BigDecimal> result = pedidoService.calcularPrecoTotal(1L);

    assertEquals(ResponseEntity.ok(BigDecimal.ZERO), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
  }

  public void testCalcularComPedidoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

    ResponseEntity<BigDecimal> result = pedidoService.calcularPrecoTotal(1L);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
  }

  public void testFecharPedido() {
    Produto produto = new Produto(1L, "Cachorro quente", BigDecimal.valueOf(3));
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setItens(List.of(new ItemPedido(pedido, produto, 1)));
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));

    ResponseEntity<String> result = pedidoService.fecharPedido(1L, BigDecimal.TEN);

    assertEquals(ResponseEntity.ok().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, times(1)).delete(any(Pedido.class));
  }

  public void testFecharPedidoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

    ResponseEntity<String> result = pedidoService.fecharPedido(1L, BigDecimal.TEN);

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, never()).delete(any(Pedido.class));
  }

  public void testFecharPedidoComPagamentoInferior() {
    pedido.setItens(List.of(new ItemPedido(pedido, produto, 1)));
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));

    ResponseEntity<String> result = pedidoService.fecharPedido(1L, BigDecimal.ZERO);

    assertEquals(ResponseEntity.badRequest().body("Valor de pagamento insuficiente"), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(pedidoRepository, never()).delete(any(Pedido.class));
  }

  public void testCalcularPrecoTotalComLista() {
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pedido));
    when(produtoRepository.findAllById(anyList())).thenReturn(List.of());

    ResponseEntity<BigDecimal> result = pedidoService.calcularPrecoTotalComLista(1L, List.of());

    assertEquals(ResponseEntity.ok(BigDecimal.ZERO), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, times(1)).findAllById(anyList());
  }

  public void testCalcularComListaPedidoInvalido() {
    when(pedidoRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
    when(produtoRepository.findAllById(anyList())).thenReturn(List.of());

    ResponseEntity<BigDecimal> result = pedidoService.calcularPrecoTotalComLista(1L, List.of());

    assertEquals(ResponseEntity.notFound().build(), result);
    verify(pedidoRepository, times(1)).findById(anyLong());
    verify(produtoRepository, never()).findAllById(anyList());
  }

  private Pedido getPedido() {
    return new Pedido(1L, List.of(getItemPedido()));
  }

  private Produto getProduto() {
    return new Produto(10L, "Cachorro quente", BigDecimal.valueOf(5));
  }

  private ItemPedido getItemPedido() {
    return new ItemPedido(getPedido(),getProduto(), 5);
  }
}