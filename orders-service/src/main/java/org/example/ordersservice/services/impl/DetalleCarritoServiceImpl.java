package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.repositories.DetalleCarritoRepository;
import org.example.ordersservice.services.DetalleCarritoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleCarritoServiceImpl implements DetalleCarritoService {

    private final DetalleCarritoRepository detalleCarritoRepository;

    @Override
    public DetalleCarrito save(DetalleCarrito detalleCarrito) {
        return detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public Page<DetalleCarrito> findAll(Pageable pageable) {
        return detalleCarritoRepository.findAll(pageable);
    }

    @Override
    public DetalleCarrito findById(Long id) {
        return detalleCarritoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de carrito no encontrado con ID: " + id));
    }

    @Override
    public Page<DetalleCarrito> findByCarritoId(Long carritoId, Pageable pageable) {
        return detalleCarritoRepository.findByCarritoId(carritoId, pageable);
    }

    @Override
    public DetalleCarrito update(Long id, DetalleCarrito detalleCarrito) {
        DetalleCarrito existingDetalleCarrito = findById(id);
        detalleCarrito.setId(existingDetalleCarrito.getId());
        return detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public void deleteById(Long id) {
        detalleCarritoRepository.deleteById(id);
    }

    @Override
    public void deleteByCarritoId(Long carritoId) {
        detalleCarritoRepository.deleteByCarritoId(carritoId);
    }
}
