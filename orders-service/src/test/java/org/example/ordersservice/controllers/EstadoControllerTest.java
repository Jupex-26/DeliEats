package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.estado.EstadoInputDto;
import org.example.ordersservice.dtos.estado.EstadoOutputDto;
import org.example.ordersservice.mappers.EstadoMapper;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.services.EstadoService;
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

@WebMvcTest(EstadoController.class)
class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoService estadoService;

    @MockBean
    private EstadoMapper estadoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        EstadoInputDto inputDto = new EstadoInputDto();
        Estado estado = new Estado();
        EstadoOutputDto outputDto = new EstadoOutputDto();

        when(estadoMapper.toEntity(any(EstadoInputDto.class))).thenReturn(estado);
        when(estadoService.save(any(Estado.class))).thenReturn(estado);
        when(estadoMapper.toDto(any(Estado.class))).thenReturn(outputDto);

        mockMvc.perform(post("/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Estado> page = new PageImpl<>(List.of(new Estado()));
        when(estadoService.findAll(any(Pageable.class))).thenReturn(page);
        when(estadoMapper.toDto(any(Estado.class))).thenReturn(new EstadoOutputDto());

        mockMvc.perform(get("/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(estadoService.findById(id)).thenReturn(new Estado());
        when(estadoMapper.toDto(any(Estado.class))).thenReturn(new EstadoOutputDto());

        mockMvc.perform(get("/estados/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        EstadoInputDto inputDto = new EstadoInputDto();
        Estado estado = new Estado();
        EstadoOutputDto outputDto = new EstadoOutputDto();

        when(estadoMapper.toEntity(any(EstadoInputDto.class))).thenReturn(estado);
        when(estadoService.update(eq(id), any(Estado.class))).thenReturn(estado);
        when(estadoMapper.toDto(any(Estado.class))).thenReturn(outputDto);

        mockMvc.perform(put("/estados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/estados/{id}", id))
                .andExpect(status().isNoContent());
    }
}
