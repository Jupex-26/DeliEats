package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.cliente.ClienteInputDto;
import org.example.ordersservice.dtos.cliente.ClienteOutputDto;
import org.example.ordersservice.models.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // Entidad -> DTO (Para enviar al Frontend)
    @Mapping(target = "nombreRol", source = "rol.nombre")
    ClienteOutputDto toDto(Cliente cliente);

    // DTO -> Entidad (Para crear el Cliente en el registro)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "carritos", ignore = true)
    Cliente toEntity(ClienteInputDto dto);
}
