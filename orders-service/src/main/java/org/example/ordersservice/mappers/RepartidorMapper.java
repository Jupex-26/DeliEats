package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.models.Repartidor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepartidorMapper {

    @Mapping(target = "nombreRol", source = "rol.nombre")
    RepartidorOutputDto toDto(Repartidor repartidor);

    @Mapping(target = "rol", ignore = true) // Se asigna ROLE_REPARTIDOR en el Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    Repartidor toEntity(RepartidorInputDto dto);
}