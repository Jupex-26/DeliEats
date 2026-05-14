package org.example.ordersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ordersservice.config.KafkaConfig;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
@Slf4j
@RequiredArgsConstructor
public class MensajeConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    
    @KafkaListener(topics = KafkaConfig.CHAT_TOPIC, groupId = "chat-group")
    public void consumeMessage(String mensajeStr) {
        log.info("Mensaje recibido vía Kafka: {}", mensajeStr);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            MensajeOutputDto mensajeDto = objectMapper.readValue(mensajeStr, MensajeOutputDto.class);
            
            // Emitir el mensaje a través de WebSockets al canal específico del receptor
            String destination = "/topic/mensajes/" + mensajeDto.getReceptorId();
            messagingTemplate.convertAndSend(destination, mensajeDto);
            
        } catch (JsonProcessingException e) {
            log.error("Error al deserializar el mensaje recibido", e);
        }
    }
}
