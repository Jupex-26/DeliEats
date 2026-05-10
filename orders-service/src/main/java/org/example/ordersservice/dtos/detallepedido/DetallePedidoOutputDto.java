package org.example.ordersservice.dtos.detallepedido;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetallePedidoOutputDto {

    private Long id;
    private Long productoId;

    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}