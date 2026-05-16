package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.cliente.ClienteInputDto;
import org.example.ordersservice.dtos.cliente.ClienteOutputDto;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.services.RepartidorService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ClienteMapper {
    @Autowired
    protected RepartidorService repartidorService;

    @Mapping(target = "repartidor", expression = "java(repartidorService.isRepartidor(cliente.getId()))")
    @Mapping(target = "nombreRol", source = "rol.nombre")
    public abstract ClienteOutputDto toDto(Cliente cliente);

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "carritos", ignore = true)
    public abstract Cliente toEntity(ClienteInputDto dto);
}
