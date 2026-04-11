package org.example.ordersservice.dtos.rol;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RolInputDto {

    @NotBlank(message = "El nombre del rol es obligatorio (ej: ROLE_CLIENTE)")
    private String nombre;
}
