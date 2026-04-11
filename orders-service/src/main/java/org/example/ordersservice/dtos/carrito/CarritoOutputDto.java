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
    private String estado; // Ej: "ACTIVO"

    private List<DetalleCarritoOutputDto> detalles;

    // Campo calculado para facilitar el trabajo al Frontend
    private BigDecimal precioTotal;
}
