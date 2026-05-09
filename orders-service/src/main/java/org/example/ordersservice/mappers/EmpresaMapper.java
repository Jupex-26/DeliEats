package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.empresa.EmpresaInputDto;
import org.example.ordersservice.dtos.empresa.EmpresaOutputDto;
import org.example.ordersservice.models.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AperturaMapper.class, ProductoMapper.class})
public abstract class EmpresaMapper {

    @Mapping(target = "nombreRol", source = "rol.nombre")
    public abstract EmpresaOutputDto toDto(Empresa empresa);


    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    public abstract Empresa toEntity(EmpresaInputDto dto);
}