package org.example.ordersservice.dtos.carrito;

import lombok.Data;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoOutputDto;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CarritoOutputDto {

    private Long id;
    private Long clienteId;
    private String nombreCliente;

    private List<DetalleCarritoOutputDto> detalles;

    private BigDecimal precioTotal;
}
