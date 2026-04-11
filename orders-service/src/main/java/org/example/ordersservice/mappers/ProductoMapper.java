package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.producto.ProductoInputDto;
import org.example.ordersservice.dtos.producto.ProductoOutputDto;
import org.example.ordersservice.models.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "empresaId", source = "empresa.id")
    @Mapping(target = "nombreEmpresa", source = "empresa.nombre")
    ProductoOutputDto toDto(Producto producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true) // Se busca en el Service por empresaId
    @Mapping(target = "cantidad", source = "cantidad")
    Producto toEntity(ProductoInputDto dto);
}