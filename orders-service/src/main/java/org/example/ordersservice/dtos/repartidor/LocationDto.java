package org.example.ordersservice.dtos.repartidor;

import lombok.Data;

@Data
public class LocationDto {
    private Long repartidorId;
    private Long pedidoId;
    private Long clienteId;
    private Double latitud;
    private Double longitud;
}
