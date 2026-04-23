package org.example.ordersservice.services;

import org.example.ordersservice.models.DetallePedido;

import java.util.List;

public interface DetallePedidoService {

    DetallePedido save(DetallePedido detallePedido);

    List<DetallePedido> findAll();

    DetallePedido findById(Long id);

    List<DetallePedido> findByPedidoId(Long pedidoId);

    DetallePedido update(Long id, DetallePedido detallePedido);

    void deleteById(Long id);

    void deleteByPedidoId(Long pedidoId);
}
