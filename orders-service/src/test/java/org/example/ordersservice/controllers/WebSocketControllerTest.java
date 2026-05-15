package org.example.ordersservice.controllers;

import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.repartidor.LocationDto;
import org.example.ordersservice.mappers.MensajeMapper;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.services.MensajeProducerService;
import org.example.ordersservice.services.MensajeService;
import org.example.ordersservice.services.LocationProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebSocketControllerTest {

    @Mock
    private MensajeService mensajeService;

    @Mock
    private MensajeMapper mensajeMapper;

    @Mock
    private MensajeProducerService mensajeProducerService;

    @Mock
    private LocationProducerService locationProducerService;

    @InjectMocks
    private WebSocketController webSocketController;

    @Test
    void sendMensajeWebsocket() {
        Long receptorId = 2L;
        MensajeInputDto inputDto = new MensajeInputDto();
        inputDto.setContenido("Hola");

        Mensaje mensaje = new Mensaje();
        when(mensajeMapper.toEntity(inputDto)).thenReturn(mensaje);
        when(mensajeService.save(mensaje)).thenReturn(mensaje);

        webSocketController.sendMensajeWebsocket(receptorId, inputDto);

        verify(mensajeService).save(any(Mensaje.class));
        verify(mensajeProducerService).sendMessage(any());
    }

    @Test
    void updateLocation() {
        LocationDto locationDto = new LocationDto();
        locationDto.setRepartidorId(1L);
        locationDto.setLatitud(10.0);
        locationDto.setLongitud(20.0);

        webSocketController.updateLocation(locationDto);

        verify(locationProducerService).sendLocation(locationDto);
    }
}
