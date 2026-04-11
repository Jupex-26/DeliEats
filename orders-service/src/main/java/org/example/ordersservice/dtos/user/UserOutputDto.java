package org.example.ordersservice.dtos.user;

import lombok.Data;

@Data
public class UserOutputDto {
    private Long id;
    private String nombre;
    private String email;
    private Long telefono;
    private String direccion;
    private String foto;
    private String nombreRol;
}
