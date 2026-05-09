package org.example.ordersservice.dtos.detallecarrito;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleCarritoOutputDto {

    private Long id;
    private Long productoId;

    private String nombreProducto;
    private String fotoProducto;
    private BigDecimal precioUnitario;

    private Integer cantidad;
}