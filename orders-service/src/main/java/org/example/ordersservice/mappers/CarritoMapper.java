package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.carrito.CarritoInputDto;
import org.example.ordersservice.dtos.carrito.CarritoOutputDto;
import org.example.ordersservice.models.Carrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetalleCarritoMapper.class})
public interface CarritoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nombreCliente", source = "cliente.nombre")
    // El campo precioTotal del DTO se quedará en null o se seteará en el Service
    CarritoOutputDto toDto(Carrito carrito);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    Carrito toEntity(CarritoInputDto dto);
}
