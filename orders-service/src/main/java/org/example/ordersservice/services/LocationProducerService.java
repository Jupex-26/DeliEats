package org.example.ordersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.config.KafkaConfig;
import org.example.ordersservice.dtos.repartidor.LocationDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendLocation(LocationDto locationDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String locationJson = objectMapper.writeValueAsString(locationDto);
            // We use repartidorId as key to ensure all updates for a delivery person go to the same partition
            kafkaTemplate.send(KafkaConfig.LOCATION_TOPIC, String.valueOf(locationDto.getRepartidorId()), locationJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing location update", e);
        }
    }
}
