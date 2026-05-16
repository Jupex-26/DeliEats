package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Repartidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable);

    Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable);
    
    Optional<Repartidor> findByClienteId(Long clienteId);
    
    boolean existsByClienteId(Long clienteId);

}
