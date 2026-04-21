package org.example.ordersservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.Carrito;
import org.example.ordersservice.repositories.CarritoRepository;
import org.example.ordersservice.services.CarritoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;

    @Override
    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    @Override
    public Carrito findById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado con ID: " + id));
    }

    @Override
    public Carrito findByClienteId(Long usuarioId) {
        return carritoRepository.findByClienteId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado con ID de usuario: " + usuarioId));
    }

    @Override
    public Carrito create(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteById(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public Carrito addProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = findById(carritoId);

       /* TODO */
        return carrito;
    }

    @Override
    public Carrito updateCantidad(Long carritoId, Long productoId, Integer nuevaCantidad) {
        Carrito carrito = findById(carritoId);

        /* TODO */

        return null;
    }

    @Override
    public Carrito removeProducto(Long carritoId, Long productoId) {
        Carrito carrito = findById(carritoId);

        /* TODO */

        return null;
    }

    @Override
    public void clearCarrito(Long carritoId) {
        Carrito carrito = findById(carritoId);

        carrito.setDetalles(new ArrayList<>());
    }

    @Override
    public Double calculateTotal(Long carritoId) {
        return 0.0;
    }
}
