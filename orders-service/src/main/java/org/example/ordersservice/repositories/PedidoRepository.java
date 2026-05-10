package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // 1. Historial del cliente (Este está bien según tu imagen)
    List<Pedido> findByCliente_IdOrderByFechaCompraDesc(Long clienteId);

    // 2. EL CAMBIO: Para buscar los pedidos de una empresa a través de sus productos
    // Usamos 'distinct' para que no repita el pedido si tiene varios productos de la misma empresa
    List<Pedido> findDistinctByDetalles_Producto_Empresa_IdAndEstado_Nombre(Long empresaId, String estadoNombre);

    // 3. Para el repartidor (Está bien, usa repartidor_id de tu imagen)
    List<Pedido> findByRepartidor_IdAndEstado_NombreNot(Long repartidorId, String estadoNombre);

    // 4. Por estado (Está bien, usa estado_id de tu imagen)
    List<Pedido> findByEstado_Nombre(String nombreEstado);

    Page<Pedido> findAllByClienteId(Long clienteId, Pageable pageable);

    Page<Pedido> findAllByEstadoId(Long estadoId, Pageable pageable);

    Page<Pedido> findAllByEmpresaId(Long empresaId, Pageable pageable);
}
