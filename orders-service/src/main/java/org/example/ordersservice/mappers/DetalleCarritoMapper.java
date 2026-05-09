package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoInputDto;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoOutputDto;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.services.ProductoService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DetalleCarritoMapper {

    @Autowired
    protected ProductoService productoService;

    @Mapping(target = "cantidad", source = "cantidad")
    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "nombreProducto", source = "producto.nombre")
    @Mapping(target = "fotoProducto", source = "producto.foto")
    @Mapping(target = "precioUnitario", source = "producto.precio")
    public abstract DetalleCarritoOutputDto toDto(DetalleCarrito detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", expression = "java(productoService.findById(dto.getProductoId()))")
    @Mapping(target = "carrito", ignore = true)
    public abstract DetalleCarrito toEntity(DetalleCarritoInputDto dto);
}
