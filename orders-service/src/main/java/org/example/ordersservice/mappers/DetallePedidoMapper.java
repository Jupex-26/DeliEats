package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;
import org.example.ordersservice.models.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class DetallePedidoMapper {

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    public abstract DetallePedidoOutputDto toDto(DetallePedido detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "producto.id", source = "productoId")
    public abstract DetallePedido toEntity(DetallePedidoInputDto dto);
}