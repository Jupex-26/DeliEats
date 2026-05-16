package org.example.ordersservice.dtos.apertura;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.ordersservice.models.Dia;

import java.time.LocalTime;

@Data
public class AperturaInputDto {

    @NotNull(message = "El día de la semana es obligatorio")
    private Dia dia;

    @NotNull(message = "La hora de apertura es obligatoria")
    private LocalTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    private LocalTime horaCierre;
}
