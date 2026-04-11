package org.example.ordersservice.dtos.cliente;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserInputDto;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteInputDto extends UserInputDto {

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDateTime fechaNacimiento;

}
