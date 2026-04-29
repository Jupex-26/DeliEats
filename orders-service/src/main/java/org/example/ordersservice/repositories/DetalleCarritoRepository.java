package org.example.ordersservice.repositories;

import org.example.ordersservice.models.DetalleCarrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    Page<DetalleCarrito> findByCarritoId(Long carritoId, Pageable pageable);

    // Borra todos los detalles de un carrito (se usa al vaciar el carrito o confirmar pedido)
    void deleteByCarritoId(Long carritoId);
}
