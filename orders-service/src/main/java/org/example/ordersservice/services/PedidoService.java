package org.example.ordersservice.services;

import org.example.ordersservice.models.Pedido;

import java.util.List;

public interface PedidoService {

    Pedido save(Pedido pedido);

    List<Pedido> findAll();

    Pedido findById(Long id);

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByEstadoId(Long estadoId);

    Pedido update(Long id, Pedido pedido);

    Pedido updateEstado(Long id, Long estadoId);

    void deleteById(Long id);

    Double calculateTotal(Long id);
}