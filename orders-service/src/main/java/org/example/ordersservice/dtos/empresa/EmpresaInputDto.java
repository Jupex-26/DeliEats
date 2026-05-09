package org.example.ordersservice.dtos.empresa;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.apertura.AperturaInputDto;
import org.example.ordersservice.dtos.user.UserInputDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmpresaInputDto extends UserInputDto {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @Email(message = "El correo de contacto debe ser válido")
    @NotBlank(message = "El correo de contacto es obligatorio")
    private String correoContacto;

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    private String telefonoContacto;

    @NotBlank(message = "Debes indicar el tipo de cocina (italiana, sushi, etc.)")
    private String tipoCocina;

    @NotEmpty(message = "La empresa debe tener al menos un horario de apertura")
    @Valid
    private List<AperturaInputDto> aperturas;

}
