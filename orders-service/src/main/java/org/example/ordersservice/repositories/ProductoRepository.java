package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Para mostrar la carta completa de un restaurante específico
    Page<Producto> findByEmpresaId(Long empresaId, Pageable pageable);

    // Para el buscador: encontrar platos por nombre en un restaurante
    Page<Producto> findByEmpresaIdAndNombreContainingIgnoreCase(Long empresaId, String nombre, Pageable pageable);

    // Para filtrar por disponibilidad (solo platos que tengan stock)
    Page<Producto> findByEmpresaIdAndCantidadGreaterThan(Long empresaId, int cantidad, Pageable pageable);

    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
