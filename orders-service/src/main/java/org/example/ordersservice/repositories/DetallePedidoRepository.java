package org.example.ordersservice.repositories;

import org.example.ordersservice.models.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Para recuperar todas las líneas de un pedido (el desglose del ticket)
    List<DetallePedido> findByPedidoId(Long pedidoId);

    // Para obtener estadísticas: ¿cuántas veces se ha vendido un producto específico?
    List<DetallePedido> findByProductoId(Long productoId);
}
