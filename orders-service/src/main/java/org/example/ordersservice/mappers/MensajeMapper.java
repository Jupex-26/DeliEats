package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.models.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MensajeMapper {

    @Mapping(target = "emisorId", source = "emisor.id")
    @Mapping(target = "nombreEmisor", source = "emisor.nombre")
    @Mapping(target = "receptorId", source = "receptor.id")
    @Mapping(target = "nombreReceptor", source = "receptor.nombre")
    MensajeOutputDto toDto(Mensaje mensaje);

    // DTO -> Entidad (Cuando alguien envía un mensaje)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emisor", ignore = true)   // Se extrae del SecurityContext en el Service
    @Mapping(target = "receptor", ignore = true) // Se busca en la DB por receptorId
    Mensaje toEntity(MensajeInputDto dto);
}