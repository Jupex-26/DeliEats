package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.repositories.PedidoRepository;
import org.example.ordersservice.services.EstadoService;
import org.example.ordersservice.services.PedidoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: "+ id));
    }

    @Override
    public List<Pedido> findByClienteId(Long clienteId) {
        return pedidoRepository.findAllByClienteId(clienteId);
    }

    @Override
    public List<Pedido> findByEstadoId(Long estadoId) {
        return pedidoRepository.findAllByEstadoId(estadoId);
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        Pedido exists = findById(id);
        pedido.setId(exists.getId());

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido updateEstado(Long id, Long estadoId) {
        Pedido pedido = findById(id);
        Estado estado = estadoService.findById(estadoId);

        pedido.cambiarEstado(estado);

        return pedidoRepository.save(pedido);
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
}
