package org.example.ordersservice.dtos.apertura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AperturaInputDto {

    @NotBlank(message = "El día de la semana es obligatorio")
    private String dia;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalDateTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalDateTime horaCierre;

    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long empresaId;
}
