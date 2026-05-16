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

    private Long repartidorId;
    private String nombreRepartidor;

    private Long empresaId;
    private String nombreEmpresa;

    private List<DetallePedidoOutputDto> detalles;
}
