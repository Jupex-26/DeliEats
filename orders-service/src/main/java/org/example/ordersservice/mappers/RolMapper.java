package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.rol.RolInputDto;
import org.example.ordersservice.dtos.rol.RolOutputDto;
import org.example.ordersservice.models.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolOutputDto toDto(Rol rol);

    @Mapping(target = "id", ignore = true)
    Rol toEntity(RolInputDto dto);
}