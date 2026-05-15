package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.cliente.ClienteInputDto;
import org.example.ordersservice.dtos.cliente.ClienteOutputDto;
import org.example.ordersservice.mappers.ClienteMapper;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.services.ClienteService;
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

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteMapper clienteMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        ClienteInputDto inputDto = new ClienteInputDto();
        Cliente cliente = new Cliente();
        ClienteOutputDto outputDto = new ClienteOutputDto();

        when(clienteMapper.toEntity(any(ClienteInputDto.class))).thenReturn(cliente);
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(outputDto);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Cliente> page = new PageImpl<>(List.of(new Cliente()));
        when(clienteService.findAll(any(Pageable.class))).thenReturn(page);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(new ClienteOutputDto());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(clienteService.findById(id)).thenReturn(new Cliente());
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(new ClienteOutputDto());

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        ClienteInputDto inputDto = new ClienteInputDto();
        Cliente cliente = new Cliente();
        ClienteOutputDto outputDto = new ClienteOutputDto();

        when(clienteMapper.toEntity(any(ClienteInputDto.class))).thenReturn(cliente);
        when(clienteService.update(eq(id), any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(outputDto);

        mockMvc.perform(put("/clientes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void solicitarSerRepartidor() throws Exception {
        Long id = 1L;
        mockMvc.perform(post("/clientes/{id}/solicitar-repartidor", id))
                .andExpect(status().isOk());
    }
}
