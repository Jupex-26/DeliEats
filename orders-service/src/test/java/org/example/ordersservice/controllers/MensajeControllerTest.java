package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.mappers.MensajeMapper;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.services.MensajeProducerService;
import org.example.ordersservice.services.MensajeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MensajeController.class)
class MensajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MensajeService mensajeService;

    @MockBean
    private MensajeMapper mensajeMapper;

    @MockBean
    private MensajeProducerService mensajeProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        MensajeInputDto inputDto = new MensajeInputDto();
        Mensaje mensaje = new Mensaje();
        MensajeOutputDto outputDto = new MensajeOutputDto();

        when(mensajeMapper.toEntity(any(MensajeInputDto.class))).thenReturn(mensaje);
        when(mensajeService.save(any(Mensaje.class))).thenReturn(mensaje);
        when(mensajeMapper.toDto(any(Mensaje.class))).thenReturn(outputDto);

        mockMvc.perform(post("/mensajes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Mensaje> page = new PageImpl<>(List.of(new Mensaje()));
        when(mensajeService.findAll(any(Pageable.class))).thenReturn(page);
        when(mensajeMapper.toDto(any(Mensaje.class))).thenReturn(new MensajeOutputDto());

        mockMvc.perform(get("/mensajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(mensajeService.findById(id)).thenReturn(new Mensaje());
        when(mensajeMapper.toDto(any(Mensaje.class))).thenReturn(new MensajeOutputDto());

        mockMvc.perform(get("/mensajes/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getChat() throws Exception {
        Long id1 = 1L;
        Long id2 = 2L;
        Page<Mensaje> page = new PageImpl<>(List.of(new Mensaje()));
        when(mensajeService.findChat(eq(id1), eq(id2), any(Pageable.class))).thenReturn(page);
        when(mensajeMapper.toDto(any(Mensaje.class))).thenReturn(new MensajeOutputDto());

        mockMvc.perform(get("/mensajes/chat")
                .param("usuario1Id", String.valueOf(id1))
                .param("usuario2Id", String.valueOf(id2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/mensajes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void markAsRead() throws Exception {
        Long id = 1L;
        mockMvc.perform(patch("/mensajes/{id}/leer", id))
                .andExpect(status().isOk());
    }
}
