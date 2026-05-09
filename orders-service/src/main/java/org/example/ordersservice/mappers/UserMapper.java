package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.user.UserInputDto;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "nombreRol", source = "rol.nombre")
    public abstract UserOutputDto toDto(User user);

    @Mapping(target = "rol", ignore = true) // El rol se asigna en el Service buscando por ID
    @Mapping(target = "id", ignore = true)
    public abstract User toEntity(UserInputDto dto);
}