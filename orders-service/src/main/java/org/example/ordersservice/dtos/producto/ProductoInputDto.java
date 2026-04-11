package org.example.ordersservice.dtos.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoInputDto {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String foto; // URL o nombre del archivo

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer cantidad;

    @NotNull(message = "Debes indicar el ID de la empresa dueña del producto")
    private Long empresaId;
}
