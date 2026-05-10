package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.tipococina.TipoCocinaInputDto;
import org.example.ordersservice.dtos.tipococina.TipoCocinaOutputDto;
import org.example.ordersservice.models.TipoCocina;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TipoCocinaMapper {

    public abstract TipoCocina toEntity(TipoCocinaInputDto dto);

    public abstract TipoCocinaOutputDto toDto(TipoCocina entity);
}
