package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.estado.EstadoInputDto;
import org.example.ordersservice.dtos.estado.EstadoOutputDto;
import org.example.ordersservice.models.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class EstadoMapper {

    public abstract EstadoOutputDto toDto(Estado estado);

    @Mapping(target = "id", ignore = true)
    public abstract Estado toEntity(EstadoInputDto dto);
}