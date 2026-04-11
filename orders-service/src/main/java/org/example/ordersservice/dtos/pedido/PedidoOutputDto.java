package org.example.ordersservice.dtos.pedido;

import lombok.Data;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoOutputDto {

    private Long id;
    private LocalDateTime fechaCompra;
    private BigDecimal precioTotal;

    private Long clienteId;
    private String nombreCliente;
    private String direccionEntrega;

    private String estadoNombre;

    // Información del Repartidor (puede ser null si no ha sido asignado)
    private Long repartidorId;
    private String nombreRepartidor;

    // Lista de productos comprados
    private List<DetallePedidoOutputDto> detalles;
}
