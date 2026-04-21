package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.repositories.DetalleCarritoRepository;
import org.example.ordersservice.services.DetalleCarritoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetalleCarritoServiceImpl implements DetalleCarritoService {
    private final DetalleCarritoRepository detalleCarritoRepository;

    @Override
    public DetalleCarrito save(DetalleCarrito detalleCarrito) {
        return detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public List<DetalleCarrito> findAll() {
        return detalleCarritoRepository.findAll();
    }

    @Override
    public DetalleCarrito findById(Long id) {
        return detalleCarritoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DetalleCarrito no encontrado con ID: " + id));
    }

    @Override
    public List<DetalleCarrito> findByCarritoId(Long carritoId) {
        return List.of();
    }

    @Override
    public DetalleCarrito update(Long id, DetalleCarrito detalleCarrito) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByCarritoId(Long carritoId) {

    }

    @Override
    public Double calculateSubtotal(Long id) {
        return 0.0;
    }
}
