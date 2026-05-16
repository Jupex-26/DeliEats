package org.example.ordersservice.dtos.repartidor;

import lombok.Data;

@Data
public class RepartidorOutputDto {

    private Long id;
    private Long clienteId;
    private String nombre;
    private String email;
    private String telefono;
    private String foto;
    private Boolean disponible;
    private Boolean aprobado;

}
