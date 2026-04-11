package org.example.ordersservice.dtos.mensaje;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MensajeOutputDto {

    private Long id;
    private String contenido;
    private LocalDateTime fechaEnvio;

    private Long emisorId;
    private String nombreEmisor;

    private Long receptorId;
    private String nombreReceptor;
}