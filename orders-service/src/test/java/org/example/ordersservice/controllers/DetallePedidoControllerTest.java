package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;
import org.example.ordersservice.mappers.DetallePedidoMapper;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.services.DetallePedidoService;
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

@WebMvcTest(DetallePedidoController.class)
class DetallePedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetallePedidoService detallePedidoService;

    @MockBean
    private DetallePedidoMapper detallePedidoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        DetallePedidoInputDto inputDto = new DetallePedidoInputDto();
        DetallePedido detalle = new DetallePedido();
        DetallePedidoOutputDto outputDto = new DetallePedidoOutputDto();

        when(detallePedidoMapper.toEntity(any(DetallePedidoInputDto.class))).thenReturn(detalle);
        when(detallePedidoService.save(any(DetallePedido.class))).thenReturn(detalle);
        when(detallePedidoMapper.toDto(any(DetallePedido.class))).thenReturn(outputDto);

        mockMvc.perform(post("/detalles-pedido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<DetallePedido> page = new PageImpl<>(List.of(new DetallePedido()));
        when(detallePedidoService.findAll(any(Pageable.class))).thenReturn(page);
        when(detallePedidoMapper.toDto(any(DetallePedido.class))).thenReturn(new DetallePedidoOutputDto());

        mockMvc.perform(get("/detalles-pedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(detallePedidoService.findById(id)).thenReturn(new DetallePedido());
        when(detallePedidoMapper.toDto(any(DetallePedido.class))).thenReturn(new DetallePedidoOutputDto());

        mockMvc.perform(get("/detalles-pedido/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByPedidoId() throws Exception {
        Long id = 1L;
        Page<DetallePedido> page = new PageImpl<>(List.of(new DetallePedido()));
        when(detallePedidoService.findByPedidoId(eq(id), any(Pageable.class))).thenReturn(page);
        when(detallePedidoMapper.toDto(any(DetallePedido.class))).thenReturn(new DetallePedidoOutputDto());

        mockMvc.perform(get("/detalles-pedido/pedido/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        DetallePedidoInputDto inputDto = new DetallePedidoInputDto();
        DetallePedido detalle = new DetallePedido();
        DetallePedidoOutputDto outputDto = new DetallePedidoOutputDto();

        when(detallePedidoMapper.toEntity(any(DetallePedidoInputDto.class))).thenReturn(detalle);
        when(detallePedidoService.update(eq(id), any(DetallePedido.class))).thenReturn(detalle);
        when(detallePedidoMapper.toDto(any(DetallePedido.class))).thenReturn(outputDto);

        mockMvc.perform(put("/detalles-pedido/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/detalles-pedido/{id}", id))
                .andExpect(status().isNoContent());
    }
}
