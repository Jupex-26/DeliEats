package org.example.ordersservice.dtos.tipococina;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoCocinaInputDto {

    @NotBlank(message = "El nombre del tipo de cocina es obligatorio")
    private String nombre;
}
