package org.example.ordersservice.services;

import org.example.ordersservice.models.DetalleCarrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetalleCarritoService {

    DetalleCarrito save(DetalleCarrito detalleCarrito);

    Page<DetalleCarrito> findAll(Pageable pageable);

    DetalleCarrito findById(Long id);

    Page<DetalleCarrito> findByCarritoId(Long carritoId, Pageable pageable);

    DetalleCarrito update(Long id, DetalleCarrito detalleCarrito);

    void deleteById(Long id);

    void deleteByCarritoId(Long carritoId);

}
