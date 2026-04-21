package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.repositories.DetallePedidoRepository;
import org.example.ordersservice.services.DetallePedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {
    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public DetallePedido save(DetallePedido detallePedido) {
        return null;
    }

    @Override
    public List<DetallePedido> findAll() {
        return List.of();
    }

    @Override
    public DetallePedido findById(Long id) {
        return null;
    }

    @Override
    public List<DetallePedido> findByPedidoId(Long pedidoId) {
        return List.of();
    }

    @Override
    public DetallePedido update(Long id, DetallePedido detallePedido) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByPedidoId(Long pedidoId) {

    }

    @Override
    public Double sumSubtotalesByPedidoId(Long pedidoId) {
        return 0.0;
    }
}
