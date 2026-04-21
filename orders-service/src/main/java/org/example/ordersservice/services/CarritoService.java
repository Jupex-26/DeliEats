package org.example.ordersservice.services;

import org.example.ordersservice.models.Carrito;

import java.util.List;

public interface CarritoService {
    List<Carrito> findAll();

    Carrito findById(Long id);

    Carrito findByClienteId(Long usuarioId);

    Carrito create(Carrito carrito);

    void deleteById(Long id);

    Carrito addProducto(Long carritoId, Long productoId, Integer cantidad);

    Carrito updateCantidad(Long carritoId, Long productoId, Integer nuevaCantidad);

    Carrito removeProducto(Long carritoId, Long productoId);

    void clearCarrito(Long carritoId);

    Double calculateTotal(Long carritoId);
}
