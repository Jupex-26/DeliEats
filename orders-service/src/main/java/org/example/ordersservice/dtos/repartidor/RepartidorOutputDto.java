package org.example.ordersservice.dtos.repartidor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserOutputDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepartidorOutputDto extends UserOutputDto {

    private Boolean disponible;

}
