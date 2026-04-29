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


    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "aperturas", ignore = true)
    Empresa toEntity(EmpresaInputDto dto);
}