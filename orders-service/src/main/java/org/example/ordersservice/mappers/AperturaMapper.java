package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.apertura.AperturaInputDto;
import org.example.ordersservice.dtos.apertura.AperturaOutputDto;
import org.example.ordersservice.models.Apertura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AperturaMapper {
    AperturaOutputDto toDto(Apertura apertura);

    @Mapping(target = "empresa", ignore = true) // Se busca y asigna en el Service mediante empresaId
    @Mapping(target = "id", ignore = true)
    Apertura toEntity(AperturaInputDto dto);
}