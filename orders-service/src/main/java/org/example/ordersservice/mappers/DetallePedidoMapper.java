package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;
import org.example.ordersservice.models.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    DetallePedidoOutputDto toDto(DetallePedido detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "producto.id", source = "productoId")
    DetallePedido toEntity(DetallePedidoInputDto dto);
}