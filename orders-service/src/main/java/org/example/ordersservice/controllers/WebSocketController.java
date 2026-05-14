package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.mappers.MensajeMapper;
import org.example.ordersservice.services.MensajeService;
import org.example.ordersservice.services.MensajeProducerService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final MensajeService mensajeService;
    private final MensajeMapper mensajeMapper;
    private final MensajeProducerService mensajeProducerService;

    // Escucha en /app/chat/{receptorId}
    @MessageMapping("/chat/{receptorId}")
    public void sendMensajeWebsocket(@DestinationVariable("receptorId") Long receptorId, @Payload MensajeInputDto dto) {
        log.info("Mensaje recibido por WebSockets destinado al usuario: {}, contenido: {}", receptorId, dto.getContenido());
        // Asegurarse de que el receptorId coincide (o forzarlo)
        dto.setReceptorId(receptorId);
        
        // Guardar en BBDD
        Mensaje entity = mensajeMapper.toEntity(dto);

        Mensaje saved = mensajeService.save(entity);
        MensajeOutputDto outputDto = mensajeMapper.toDto(saved);

        // Enviar a Kafka
        mensajeProducerService.sendMessage(outputDto);
    }
}
