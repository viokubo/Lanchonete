package com.lanchonete.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.dto.ItensDTO;
import com.lanchonete.entities.ItemPedido;
import com.lanchonete.entities.Pedido;
import com.lanchonete.entities.Produto;
import com.lanchonete.repositories.PedidoRepository;
import com.lanchonete.repositories.ProdutoRepository;
import com.lanchonete.services.PedidoService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PedidoControllerTest extends TestCase {
  @Mock
  private PedidoService pedidoService;

  @InjectMocks
  private PedidoController pedidoController;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  public void testCriarPedido() {
    Long pedidoId = 1L;
    when(pedidoService.criarPedido()).thenReturn(pedidoId);

    ResponseEntity<Long> result = pedidoController.criarPedido();

    assertEquals(ResponseEntity.ok(pedidoId), result);
    verify(pedidoService, times(1)).criarPedido();
  }

  public void testAdicionarProduto() {
    ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
    when(pedidoService.adicionarProduto(anyLong(), anyLong(), anyInt())).thenReturn(expectedResponse);

    ResponseEntity<Void> result = pedidoController.adicionarProduto(1L, 1L, 2);

    assertEquals(expectedResponse, result);
    verify(pedidoService, times(1)).adicionarProduto(anyLong(), anyLong(), anyInt());
  }

  public void testRemoverProduto() {
    ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
    when(pedidoService.removerProduto(anyLong(), anyLong(), anyInt())).thenReturn(expectedResponse);

    ResponseEntity<Void> result = pedidoController.removerProduto(1L, 1L, 2);

    assertEquals(expectedResponse, result);
    verify(pedidoService, times(1)).removerProduto(anyLong(), anyLong(), anyInt());
  }

  public void testCalcularPrecoTotal() {
    BigDecimal precoTotal = BigDecimal.TEN;
    ResponseEntity<BigDecimal> expectedResponse = ResponseEntity.ok(precoTotal);
    when(pedidoService.calcularPrecoTotal(anyLong())).thenReturn(expectedResponse);

    ResponseEntity<BigDecimal> result = pedidoController.calcularPrecoTotal(1L);

    assertEquals(expectedResponse, result);
    verify(pedidoService, times(1)).calcularPrecoTotal(anyLong());
  }

  public void testFecharPedido() {
    ResponseEntity<String> expectedResponse = ResponseEntity.ok().build();
    when(pedidoService.fecharPedido(anyLong(), any(BigDecimal.class))).thenReturn(expectedResponse);

    ResponseEntity<String> result = pedidoController.fecharPedido(1L, BigDecimal.TEN);

    assertEquals(expectedResponse, result);
    verify(pedidoService, times(1)).fecharPedido(anyLong(), any(BigDecimal.class));
  }

  public void testCalcularPrecoTotalComLista() {
    BigDecimal precoTotal = BigDecimal.TEN;
    ResponseEntity<BigDecimal> expectedResponse = ResponseEntity.ok(precoTotal);
    when(pedidoService.calcularPrecoTotalComLista(anyLong(), anyList())).thenReturn(expectedResponse);

    ResponseEntity<BigDecimal> result = pedidoController.calcularPrecoTotalComLista(1L, List.of());

    assertEquals(expectedResponse, result);
    verify(pedidoService, times(1)).calcularPrecoTotalComLista(anyLong(), anyList());
  }
}