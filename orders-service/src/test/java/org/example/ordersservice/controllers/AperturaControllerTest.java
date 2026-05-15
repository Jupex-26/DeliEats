package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.apertura.AperturaInputDto;
import org.example.ordersservice.dtos.apertura.AperturaOutputDto;
import org.example.ordersservice.mappers.AperturaMapper;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.services.AperturaService;
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

@WebMvcTest(AperturaController.class)
class AperturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AperturaService aperturaService;

    @MockBean
    private AperturaMapper aperturaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        AperturaInputDto inputDto = new AperturaInputDto();
        Apertura apertura = new Apertura();
        AperturaOutputDto outputDto = new AperturaOutputDto();

        when(aperturaMapper.toEntity(any(AperturaInputDto.class))).thenReturn(apertura);
        when(aperturaService.save(any(Apertura.class))).thenReturn(apertura);
        when(aperturaMapper.toDto(any(Apertura.class))).thenReturn(outputDto);

        mockMvc.perform(post("/aperturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Apertura> page = new PageImpl<>(List.of(new Apertura()));
        when(aperturaService.findAll(any(Pageable.class))).thenReturn(page);
        when(aperturaMapper.toDto(any(Apertura.class))).thenReturn(new AperturaOutputDto());

        mockMvc.perform(get("/aperturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        Apertura apertura = new Apertura();
        AperturaOutputDto outputDto = new AperturaOutputDto();
        outputDto.setId(id);

        when(aperturaService.findById(id)).thenReturn(apertura);
        when(aperturaMapper.toDto(apertura)).thenReturn(outputDto);

        mockMvc.perform(get("/aperturas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        AperturaInputDto inputDto = new AperturaInputDto();
        Apertura apertura = new Apertura();
        AperturaOutputDto outputDto = new AperturaOutputDto();

        when(aperturaMapper.toEntity(any(AperturaInputDto.class))).thenReturn(apertura);
        when(aperturaService.update(eq(id), any(Apertura.class))).thenReturn(apertura);
        when(aperturaMapper.toDto(any(Apertura.class))).thenReturn(outputDto);

        mockMvc.perform(put("/aperturas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/aperturas/{id}", id))
                .andExpect(status().isNoContent());
    }
}
