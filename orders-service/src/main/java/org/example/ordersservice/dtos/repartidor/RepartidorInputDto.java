package org.example.ordersservice.dtos.repartidor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RepartidorInputDto {

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El estado de disponibilidad inicial es obligatorio")
    private Boolean disponible = true;

}
