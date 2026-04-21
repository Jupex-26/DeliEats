package org.example.ordersservice.services;

import org.example.ordersservice.models.DetalleCarrito;

import java.util.List;

public interface DetalleCarritoService {

    DetalleCarrito save(DetalleCarrito detalleCarrito);

    List<DetalleCarrito> findAll();

    DetalleCarrito findById(Long id);

    List<DetalleCarrito> findByCarritoId(Long carritoId);

    DetalleCarrito update(Long id, DetalleCarrito detalleCarrito);

    void deleteById(Long id);

    void deleteByCarritoId(Long carritoId);

    Double calculateSubtotal(Long id);
}