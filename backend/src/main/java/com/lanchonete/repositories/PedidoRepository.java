package com.lanchonete.repositories;

import com.lanchonete.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Modifying
    @Query(value = " DELETE FROM item_pedido ip WHERE ip.id = :itemPedidoId ")
    void deleteItemPedidoById(@Param("itemPedidoId") Long itemPedidoId);
}
