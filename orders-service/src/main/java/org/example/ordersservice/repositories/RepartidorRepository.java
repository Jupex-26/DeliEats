package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Repartidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable);

    Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable);
    
    @Query("SELECT r FROM Repartidor r WHERE r.aprobado = :aprobado AND " +
           "(LOWER(r.cliente.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.cliente.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Repartidor> searchByAprobado(@Param("aprobado") boolean aprobado, @Param("search") String search, Pageable pageable);
    
    Optional<Repartidor> findByClienteId(Long clienteId);
    
    boolean existsByClienteId(Long clienteId);

}
