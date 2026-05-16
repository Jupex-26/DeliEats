package org.example.ordersservice.services;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.config.KafkaConfig;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
@RequiredArgsConstructor
public class MensajeProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(MensajeOutputDto mensajeDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String mensajeJson = objectMapper.writeValueAsString(mensajeDto);
            kafkaTemplate.send(KafkaConfig.CHAT_TOPIC, String.valueOf(mensajeDto.getReceptorId()), mensajeJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar el mensaje", e);
        }
    }
}
