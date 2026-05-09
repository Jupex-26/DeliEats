package org.example.ordersservice.dtos.detallecarrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleCarritoInputDto {
    @NotNull(message = "El ID del carrito es obligatorio")
    private Long carritoId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    private Integer cantidad;
}