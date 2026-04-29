package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.repositories.DetallePedidoRepository;
import org.example.ordersservice.services.DetallePedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public DetallePedido save(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public Page<DetallePedido> findAll(Pageable pageable) {
        return detallePedidoRepository.findAll(pageable);
    }

    @Override
    public DetallePedido findById(Long id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Detalle de pedido no encontrado con ID: " + id));
    }

    @Override
    public Page<DetallePedido> findByPedidoId(Long pedidoId, Pageable pageable) {
        return detallePedidoRepository.findByPedidoId(pedidoId, pageable);
    }

    @Override
    public DetallePedido update(Long id, DetallePedido detallePedido) {
        DetallePedido existingDetallePedido = findById(id);
        detallePedido.setId(existingDetallePedido.getId());
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
