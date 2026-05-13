package org.example.ordersservice.services;

import org.example.ordersservice.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PedidoService {

    Pedido save(Pedido pedido);

    Page<Pedido> findAll(Pageable pageable);

    Pedido findById(Long id);

    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    Page<Pedido> findByEstadoId(Long estadoId, Pageable pageable);

    Page<Pedido> findByEmpresaId(Long empresaId, Pageable pageable);

    Pedido update(Long id, Pedido pedido);

    Pedido updateEstado(Long id, Long estadoId);
    
    Pedido cancelarPedido(Long id);

    void deleteById(Long id);

}
