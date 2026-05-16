package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.producto.ProductoInputDto;
import org.example.ordersservice.dtos.producto.ProductoOutputDto;
import org.example.ordersservice.models.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ProductoMapper {

    @Mapping(target = "empresaId", source = "empresa.id")
    @Mapping(target = "nombreEmpresa", source = "empresa.nombre")
    public abstract ProductoOutputDto toDto(Producto producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa.id", source = "empresaId")
    @Mapping(target = "cantidad", source = "cantidad")
    public abstract Producto toEntity(ProductoInputDto dto);
}