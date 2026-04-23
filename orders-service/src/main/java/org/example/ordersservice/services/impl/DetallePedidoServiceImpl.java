package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
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
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public DetallePedido findById(Long id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle Pedido no encontrado con ID: " + id));
    }

    @Override
    public List<DetallePedido> findByPedidoId(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    @Override
    public DetallePedido update(Long id, DetallePedido detallePedido) {
        DetallePedido exists = findById(id);
        detallePedido.setId(exists.getId());

        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void deleteById(Long id) {
        detallePedidoRepository.deleteById(id);
    }

    @Override
    public void deleteByPedidoId(Long pedidoId) {
        detallePedidoRepository.deleteByPedidoId(pedidoId);
    }
}
