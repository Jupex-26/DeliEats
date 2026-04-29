package org.example.ordersservice.services;

import org.example.ordersservice.models.Carrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CarritoService {
    Page<Carrito> findAll(Pageable pageable);

    Carrito findById(Long id);

    Carrito findByClienteId(Long usuarioId);

    Carrito create(Carrito carrito);

    void deleteById(Long id);

    Carrito addProducto(Long carritoId, Long productoId, Integer cantidad);

    Carrito removeProducto(Long carritoId, Long productoId);

    void clearCarrito(Long carritoId);

    BigDecimal calculateTotal(Long carritoId);
}
