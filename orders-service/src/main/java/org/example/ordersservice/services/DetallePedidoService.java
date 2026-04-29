package org.example.ordersservice.services;

import org.example.ordersservice.models.DetallePedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetallePedidoService {

    DetallePedido save(DetallePedido detallePedido);

    Page<DetallePedido> findAll(Pageable pageable);

    DetallePedido findById(Long id);

    Page<DetallePedido> findByPedidoId(Long pedidoId, Pageable pageable);

    DetallePedido update(Long id, DetallePedido detallePedido);

    void deleteById(Long id);

    void deleteByPedidoId(Long pedidoId);
}
