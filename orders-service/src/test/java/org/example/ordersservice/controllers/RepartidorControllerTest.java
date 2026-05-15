package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.mappers.RepartidorMapper;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.services.RepartidorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepartidorController.class)
class RepartidorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepartidorService repartidorService;

    @MockBean
    private RepartidorMapper repartidorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        RepartidorInputDto inputDto = new RepartidorInputDto();
        Repartidor repartidor = new Repartidor();
        RepartidorOutputDto outputDto = new RepartidorOutputDto();

        when(repartidorMapper.toEntity(any(RepartidorInputDto.class))).thenReturn(repartidor);
        when(repartidorService.save(any(Repartidor.class))).thenReturn(repartidor);
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(outputDto);

        mockMvc.perform(post("/repartidores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Repartidor> page = new PageImpl<>(List.of(new Repartidor()));
        when(repartidorService.findAll(any(Pageable.class))).thenReturn(page);
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(new RepartidorOutputDto());

        mockMvc.perform(get("/repartidores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(repartidorService.findById(id)).thenReturn(new Repartidor());
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(new RepartidorOutputDto());

        mockMvc.perform(get("/repartidores/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByDisponible() throws Exception {
        Page<Repartidor> page = new PageImpl<>(List.of(new Repartidor()));
        when(repartidorService.findByDisponible(eq(true), any(Pageable.class))).thenReturn(page);
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(new RepartidorOutputDto());

        mockMvc.perform(get("/repartidores/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByAprobado() throws Exception {
        boolean aprobado = false;
        Page<Repartidor> page = new PageImpl<>(List.of(new Repartidor()));
        when(repartidorService.findByAprobado(eq(aprobado), any(Pageable.class))).thenReturn(page);
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(new RepartidorOutputDto());

        mockMvc.perform(get("/repartidores/aprobado")
                .param("aprobado", String.valueOf(aprobado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        RepartidorInputDto inputDto = new RepartidorInputDto();
        Repartidor repartidor = new Repartidor();
        RepartidorOutputDto outputDto = new RepartidorOutputDto();

        when(repartidorMapper.toEntity(any(RepartidorInputDto.class))).thenReturn(repartidor);
        when(repartidorService.update(eq(id), any(Repartidor.class))).thenReturn(repartidor);
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(outputDto);

        mockMvc.perform(put("/repartidores/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDisponibilidad() throws Exception {
        Long id = 1L;
        boolean disponible = true;
        when(repartidorService.updateDisponibilidad(id, disponible)).thenReturn(new Repartidor());
        when(repartidorMapper.toDto(any(Repartidor.class))).thenReturn(new RepartidorOutputDto());

        mockMvc.perform(patch("/repartidores/{id}/disponibilidad")
                .param("disponible", String.valueOf(disponible)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/repartidores/{id}", id))
                .andExpect(status().isNoContent());
    }
}
