package org.example.ordersservice.dtos.mensaje;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensajeInputDto {

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String contenido;

    @NotNull(message = "Debes indicar el receptor del mensaje")
    private Long receptorId;

    // Nota: El emisorId NO se pide aquí, se extrae del JWT en el backend por seguridad.
}