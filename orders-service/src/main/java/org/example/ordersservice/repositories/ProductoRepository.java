package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Para mostrar la carta completa de un restaurante específico
    List<Producto> findByEmpresaId(Long empresaId);

    // Para el buscador: encontrar platos por nombre en un restaurante
    List<Producto> findByEmpresaIdAndNombreContainingIgnoreCase(Long empresaId, String nombre);

    // Para filtrar por disponibilidad (solo platos que tengan stock)
    List<Producto> findByEmpresaIdAndCantidadGreaterThan(Long empresaId, int cantidad);
}
