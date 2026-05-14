package org.example.ordersservice.mappers;

import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.models.Mensaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MensajeMapper {

    @Mapping(target = "emisorId", source = "emisor.id")
    @Mapping(target = "nombreEmisor", source = "emisor.nombre")
    @Mapping(target = "receptorId", source = "receptor.id")
    @Mapping(target = "nombreReceptor", source = "receptor.nombre")
    @Mapping(target = "contenido", source = "mensaje")
    @Mapping(target = "fechaEnvio", source = "fecha")
    public abstract MensajeOutputDto toDto(Mensaje mensaje);

    // DTO -> Entidad (Cuando alguien envía un mensaje)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "leido", constant = "false")
    @Mapping(target = "emisor.id", source = "emisorId")
    @Mapping(target = "receptor.id", source = "receptorId")
    @Mapping(target = "mensaje", source = "contenido") // Este es el mapeo que debe hacer que funcione
    public abstract Mensaje toEntity(MensajeInputDto dto);
}
