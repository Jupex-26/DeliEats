package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.empresa.EmpresaInputDto;
import org.example.ordersservice.dtos.empresa.EmpresaOutputDto;
import org.example.ordersservice.models.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface EmpresaMapper {

    @Mapping(target = "nombreRol", source = "rol.nombre")
    EmpresaOutputDto toDto(Empresa empresa);

    @Mapping(target = "rol", ignore = true) // Se asigna el rol de Empresa en el Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true) // Los productos se gestionan aparte
    @Mapping(target = "password", source = "password")
    Empresa toEntity(EmpresaInputDto dto);
}