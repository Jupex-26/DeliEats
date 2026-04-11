package org.example.ordersservice.dtos.apertura;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AperturaOutputDto {

    private Long id;
    private String dia;
    private LocalDateTime horaApertura;
    private LocalDateTime horaCierre;
    private Long empresaId;
    private String nombreEmpresa;
}
