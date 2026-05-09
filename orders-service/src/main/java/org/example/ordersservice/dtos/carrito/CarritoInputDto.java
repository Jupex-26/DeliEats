package org.example.ordersservice.dtos.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoInputDto;

import java.util.List;

@Data
public class CarritoInputDto {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    private List<DetalleCarritoInputDto> detalles;
}
