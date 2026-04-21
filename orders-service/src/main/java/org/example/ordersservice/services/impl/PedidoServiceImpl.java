package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.repositories.PedidoRepository;
import org.example.ordersservice.services.PedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;

    @Override
    public Pedido save(Pedido pedido) {
        return null;
    }

    @Override
    public List<Pedido> findAll() {
        return List.of();
    }

    @Override
    public Pedido findById(Long id) {
        return null;
    }

    @Override
    public List<Pedido> findByClienteId(Long clienteId) {
        return List.of();
    }

    @Override
    public List<Pedido> findByEstadoId(Long estadoId) {
        return List.of();
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        return null;
    }

    @Override
    public Pedido updateEstado(Long id, Long estadoId) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Double calculateTotal(Long id) {
        return 0.0;
    }
}
