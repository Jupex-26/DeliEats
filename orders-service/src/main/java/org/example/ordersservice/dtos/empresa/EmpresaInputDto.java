package org.example.ordersservice.dtos.empresa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserInputDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmpresaInputDto extends UserInputDto {

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Email(message = "El correo de contacto debe ser válido")
    private String correoContacto;

    private String telefonoContacto;

    @NotBlank(message = "Debes indicar el tipo de cocina (italiana, sushi, etc.)")
    private String tipoCocina;

}
