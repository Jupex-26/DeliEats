package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.pedido.PedidoInputDto;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetallePedidoMapper.class})
public abstract class PedidoMapper {

    @Mapping(target = "precioTotal", source = "precio")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nombreCliente", source = "cliente.nombre")
    @Mapping(target = "direccionEntrega", source = "cliente.direccion")
    @Mapping(target = "estadoNombre", source = "estado.nombre")
    @Mapping(target = "repartidorId", source = "repartidor.id")
    @Mapping(target = "nombreRepartidor", source = "repartidor.nombre")
    // El campo 'precioTotal' se setea en el Service tras calcular la suma de los detalles
    public abstract PedidoOutputDto toDto(Pedido pedido);

    @Mapping(target = "precio", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCompra", ignore = true) // Se asigna en el Service: LocalDateTime.now()
    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "repartidor.id", source = "idRepartidor")
    @Mapping(target = "estado.id", source = "idEstado")      // Se asigna el estado inicial "PENDIENTE"
    @Mapping(target = "detalles", source = "detalles")    // Se procesan los detalles uno a uno en el Service
    public abstract Pedido toEntity(PedidoInputDto dto);
}
