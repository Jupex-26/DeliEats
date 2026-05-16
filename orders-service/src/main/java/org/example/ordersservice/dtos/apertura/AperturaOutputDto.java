package org.example.ordersservice.dtos.apertura;

import lombok.Data;
import org.example.ordersservice.models.Dia;

import java.time.LocalTime;

@Data
public class AperturaOutputDto {

    private Long id;
    private Dia dia;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
}
