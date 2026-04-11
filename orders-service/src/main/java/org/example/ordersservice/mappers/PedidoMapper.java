package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.pedido.PedidoInputDto;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetallePedidoMapper.class})
public interface PedidoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nombreCliente", source = "cliente.nombre")
    @Mapping(target = "direccionEntrega", source = "cliente.direccion")
    @Mapping(target = "estadoNombre", source = "estado.nombre")
    @Mapping(target = "repartidorId", source = "repartidor.id")
    @Mapping(target = "nombreRepartidor", source = "repartidor.nombre")
    // El campo 'precioTotal' se setea en el Service tras calcular la suma de los detalles
    PedidoOutputDto toDto(Pedido pedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCompra", ignore = true) // Se asigna en el Service: LocalDateTime.now()
    @Mapping(target = "cliente", ignore = true)     // Se busca en DB por clienteId
    @Mapping(target = "repartidor", ignore = true)  // Se asigna después cuando alguien lo acepta
    @Mapping(target = "estado", ignore = true)      // Se asigna el estado inicial "PENDIENTE"
    @Mapping(target = "detalles", ignore = true)    // Se procesan los detalles uno a uno en el Service
    Pedido toEntity(PedidoInputDto dto);
}
