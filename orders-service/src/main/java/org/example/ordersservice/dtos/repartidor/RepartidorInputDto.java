package org.example.ordersservice.dtos.repartidor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserInputDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepartidorInputDto extends UserInputDto {

    @NotNull(message = "El estado de disponibilidad inicial es obligatorio")
    private Boolean disponible = true;

}
