package org.example.ordersservice.dtos.cliente;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserOutputDto;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteOutputDto extends UserOutputDto {

    private LocalDateTime fechaNacimiento;
    private boolean isRepartidor;

}