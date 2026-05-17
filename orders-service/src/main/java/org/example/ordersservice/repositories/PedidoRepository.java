package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAllByClienteId(Long clienteId, Pageable pageable);

    Page<Pedido> findAllByEmpresaId(Long empresaId, Pageable pageable);

    Page<Pedido> findAllByEmpresaIdAndFechaCompraBetween(Long empresaId, LocalDateTime inicioMes, LocalDateTime finMes, Pageable pageable);
    
    @Query("SELECT p FROM Pedido p WHERE " +
           "(CAST(p.id AS string) = :search OR " +
           "CAST(p.cliente.id AS string) = :search OR " +
           "LOWER(p.cliente.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.cliente.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.empresa.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.estado.nombre) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Pedido> searchGlobal(String search, Pageable pageable);
}
