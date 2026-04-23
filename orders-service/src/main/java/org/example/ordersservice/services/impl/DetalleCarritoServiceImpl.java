package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.repositories.DetalleCarritoRepository;
import org.example.ordersservice.services.DetalleCarritoService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return detalleCarritoRepository.findByCarritoId(carritoId);
    }

    @Override
    public DetalleCarrito update(Long id, DetalleCarrito detalleCarrito) {
        DetalleCarrito exists = findById(id);

        detalleCarrito.setId(exists.getId());

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
