package org.example.ordersservice.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInputDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_\\-]).*$", message = "La contraseña debe contener al menos un dígito, una letra minúscula, una mayúscula y un carácter especial")
    private String password;

    @NotNull(message = "El teléfono es obligatorio")
    private Long telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String foto;

    @NotNull(message = "El rol es obligatorio")
    private Long rolId;
}
