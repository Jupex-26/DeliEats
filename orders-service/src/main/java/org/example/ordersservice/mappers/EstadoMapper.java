package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.estado.EstadoInputDto;
import org.example.ordersservice.dtos.estado.EstadoOutputDto;
import org.example.ordersservice.models.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoOutputDto toDto(Estado estado);

    @Mapping(target = "id", ignore = true)
    Estado toEntity(EstadoInputDto dto);
}