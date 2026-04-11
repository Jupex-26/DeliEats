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
    // El subtotal se calculará en el Service para mantener la pureza
    DetallePedidoOutputDto toDto(DetallePedido detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)   // Se asigna en el Service
    @Mapping(target = "producto", ignore = true) // Se asigna en el Service
    DetallePedido toEntity(DetallePedidoInputDto dto);
}