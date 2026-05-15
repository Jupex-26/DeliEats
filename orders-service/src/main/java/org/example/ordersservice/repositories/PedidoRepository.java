package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAllByClienteId(Long clienteId, Pageable pageable);

    Page<Pedido> findAllByEmpresaId(Long empresaId, Pageable pageable);

    Page<Pedido> findAllByEmpresaIdAndFechaCompraBetween(Long empresaId, LocalDateTime inicioMes, LocalDateTime finMes, Pageable pageable);
}
