package org.example.ordersservice.dtos.pedido;

import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoInputDto {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long empresaId;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    private List<DetallePedidoInputDto> detalles;

    private Long idRepartidor;
    private Long idEstado;

}