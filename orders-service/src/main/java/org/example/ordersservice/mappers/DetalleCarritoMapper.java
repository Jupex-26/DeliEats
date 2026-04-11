package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoInputDto;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoOutputDto;
import org.example.ordersservice.models.DetalleCarrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleCarritoMapper {

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    @Mapping(target = "fotoProducto", source = "producto.foto")
    @Mapping(target = "precioUnitario", source = "producto.precio")
    // El campo 'subtotal' del DTO se calculará y seteará en la capa de Service
    DetalleCarritoOutputDto toDto(DetalleCarrito detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true) // Se busca y asigna en el Service usando productoId
    @Mapping(target = "carrito", ignore = true)  // Se busca y asigna en el Service usando carritoId
    DetalleCarrito toEntity(DetalleCarritoInputDto dto);
}
