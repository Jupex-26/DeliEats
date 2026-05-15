package org.example.ordersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ordersservice.config.KafkaConfig;
import org.example.ordersservice.dtos.repartidor.LocationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = KafkaConfig.LOCATION_TOPIC, groupId = "location-group")
    public void consumeLocation(String locationStr) {
        log.info("Location update received via Kafka: {}", locationStr);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LocationDto locationDto = objectMapper.readValue(locationStr, LocationDto.class);

            // Send to the specific client tracking this order/delivery
            if (locationDto.getClienteId() != null) {
                String clientDestination = "/topic/location/" + locationDto.getClienteId();
                messagingTemplate.convertAndSend(clientDestination, locationDto);
            }
            
            // Also send to a generic topic for the specific order if anyone else is tracking
            if (locationDto.getPedidoId() != null) {
                String orderDestination = "/topic/pedido/" + locationDto.getPedidoId() + "/location";
                messagingTemplate.convertAndSend(orderDestination, locationDto);
            }

        } catch (JsonProcessingException e) {
            log.error("Error deserializing location update", e);
        }
    }
}
