package org.example.ordersservice.services;

import org.example.ordersservice.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    Pedido save(Pedido pedido);

    Page<Pedido> findAll(Pageable pageable);

    Pedido findById(Long id);

    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    Page<Pedido> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Pedido> findByEmpresaIdMesActual(Long empresaId, Pageable pageable);

    Page<Pedido> findByEmpresaIdAndMesAndAnio(Long empresaId, int mes, int anio, Pageable pageable);

    Pedido update(Long id, Pedido pedido);

    Pedido updateEstado(Long id, Long estadoId);
    
    Pedido cancelarPedido(Long id);

    void deleteById(Long id);

}
