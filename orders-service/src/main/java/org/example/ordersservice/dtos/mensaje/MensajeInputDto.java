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

    @NotNull(message = "Debes indicar el emisor del mensaje")
    private Long emisorId;

}
