package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    /**
     * Busca un estado por su nombre exacto.
     * Fundamental para asignar el estado inicial "PENDIENTE" al crear un pedido.
     */
    Optional<Estado> findByNombre(String nombre);
}