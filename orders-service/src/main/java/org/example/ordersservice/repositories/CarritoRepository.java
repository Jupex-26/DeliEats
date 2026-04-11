package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /**
     * Busca el carrito activo de un cliente específico.
     * Como cada cliente solo debería tener un único carrito.
     */
    Optional<Carrito> findByClienteId(Long clienteId);

    /**
     * Verifica si un cliente ya tiene un carrito creado.
     */
    boolean existsByClienteId(Long clienteId);
}
