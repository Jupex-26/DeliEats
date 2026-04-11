package org.example.ordersservice.dtos.estado;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoInputDto {

    @NotBlank(message = "El nombre del estado no puede estar vacío")
    private String nombre;
}
