package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.models.Repartidor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RepartidorMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nombre", source = "cliente.nombre")
    @Mapping(target = "email", source = "cliente.email")
    @Mapping(target = "telefono", source = "cliente.telefono")
    @Mapping(target = "foto", source = "cliente.foto")
    public abstract RepartidorOutputDto toDto(Repartidor repartidor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "aprobado", ignore = true)
    public abstract Repartidor toEntity(RepartidorInputDto dto);
}
