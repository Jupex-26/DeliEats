package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.repositories.PedidoRepository;
import org.example.ordersservice.services.EstadoService;
import org.example.ordersservice.services.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final EstadoService estadoService;

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public Page<Pedido> findByClienteId(Long clienteId, Pageable pageable) {
        return pedidoRepository.findAllByClienteId(clienteId, pageable);
    }

    @Override
    public Page<Pedido> findByEstadoId(Long estadoId, Pageable pageable) {
        return pedidoRepository.findAllByEstadoId(estadoId, pageable);
    }

    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public BigDecimal calculateTotal(Long id) {
        Pedido pedido = findById(id);
        return pedido.calcularTotal();
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        Pedido pedidoUpdated = findById(id);
        
        pedido.setId(pedidoUpdated.getId());
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido updateEstado(Long id, Long estadoId) {
        Pedido pedido = findById(id);
        Estado nuevoEstado = estadoService.findById(estadoId);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
