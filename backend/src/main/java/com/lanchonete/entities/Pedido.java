package com.lanchonete.entities;

import com.lanchonete.dto.ItensDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity(name = "pedido")
@Table(name = "pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    public BigDecimal calcularPrecoTotal() {
        BigDecimal precoTotal = BigDecimal.ZERO;
        for (ItemPedido itemPedido : itens) {
            BigDecimal precoItem = itemPedido.getProduto().getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
            precoTotal = precoTotal.add(precoItem);
        }
        return precoTotal;
    }

    public BigDecimal calcularPrecoTotalComLista(List<ItensDTO> itens, List<Produto> produtos) {
        BigDecimal precoTotal = BigDecimal.ZERO;
        Map<Long, ItensDTO> mapProdutosByProdutoId = itens.stream().collect(Collectors.toMap(ItensDTO::getProdutoId, Function.identity()));
        for (Produto produto : produtos) {
            BigDecimal precoItem = produto.getPreco().multiply(BigDecimal.valueOf(mapProdutosByProdutoId.get(produto.getId()).getQuantidade()));
            precoTotal = precoTotal.add(precoItem);
        }
        return precoTotal;
    }
}