package org.example.ordersservice.repositories;

import org.example.ordersservice.models.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    List<DetalleCarrito> findByCarritoId(Long carritoId);

    // Útil para saber si un producto específico ya está en el carrito (y así solo sumar cantidad)
    Optional<DetalleCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

    // Borra todos los detalles de un carrito (se usa al vaciar el carrito o confirmar pedido)
    void deleteByCarritoId(Long carritoId);
}