package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.carrito.CarritoInputDto;
import org.example.ordersservice.dtos.carrito.CarritoOutputDto;
import org.example.ordersservice.models.Carrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetalleCarritoMapper.class})

public abstract class CarritoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nombreCliente", source = "cliente.nombre")
    @Mapping(target = "precioTotal", expression = "java(carrito.getPrecioTotal())")
    public abstract CarritoOutputDto toDto(Carrito carrito);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente.id", source = "clienteId")
    public abstract Carrito toEntity(CarritoInputDto dto);
}
