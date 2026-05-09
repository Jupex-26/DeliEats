package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.models.Repartidor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RepartidorMapper {

    @Mapping(target = "nombreRol", source = "rol.nombre")
    public abstract RepartidorOutputDto toDto(Repartidor repartidor);


    @Mapping(target = "pedidos", ignore = true)
    public abstract Repartidor toEntity(RepartidorInputDto dto);
}