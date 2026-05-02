package org.example.ordersservice.repositories;

import org.example.ordersservice.models.DetallePedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Para recuperar todas las líneas de un pedido (el desglose del ticket)
    Page<DetallePedido> findByPedidoId(Long pedidoId, Pageable pageable);

    @Modifying
    void deleteByPedidoId(Long pedidoId);
}
