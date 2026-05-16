package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.empresa.EmpresaInputDto;
import org.example.ordersservice.dtos.empresa.EmpresaOutputDto;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.TipoCocina;
import org.example.ordersservice.services.TipoCocinaService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AperturaMapper.class, ProductoMapper.class, TipoCocinaMapper.class})
public abstract class EmpresaMapper {

    @Autowired
    protected TipoCocinaService tipoCocinaService;

    @Mapping(target = "nombreRol", source = "rol.nombre")
    public abstract EmpresaOutputDto toDto(Empresa empresa);


    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "tipoCocina", expression = "java(mapTipoCocina(dto.getTipoCocinaId()))")
    public abstract Empresa toEntity(EmpresaInputDto dto);
    
    protected TipoCocina mapTipoCocina(Long id) {
        if (id == null) {
            return null;
        }
        return tipoCocinaService.findById(id);
    }
}
