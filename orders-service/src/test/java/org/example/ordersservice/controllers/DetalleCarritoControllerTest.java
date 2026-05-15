package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoInputDto;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoOutputDto;
import org.example.ordersservice.mappers.DetalleCarritoMapper;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.services.DetalleCarritoService;
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

@WebMvcTest(DetalleCarritoController.class)
class DetalleCarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleCarritoService detalleCarritoService;

    @MockBean
    private DetalleCarritoMapper detalleCarritoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        DetalleCarritoInputDto inputDto = new DetalleCarritoInputDto();
        DetalleCarrito detalle = new DetalleCarrito();
        DetalleCarritoOutputDto outputDto = new DetalleCarritoOutputDto();

        when(detalleCarritoMapper.toEntity(any(DetalleCarritoInputDto.class))).thenReturn(detalle);
        when(detalleCarritoService.save(any(DetalleCarrito.class))).thenReturn(detalle);
        when(detalleCarritoMapper.toDto(any(DetalleCarrito.class))).thenReturn(outputDto);

        mockMvc.perform(post("/detalles-carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<DetalleCarrito> page = new PageImpl<>(List.of(new DetalleCarrito()));
        when(detalleCarritoService.findAll(any(Pageable.class))).thenReturn(page);
        when(detalleCarritoMapper.toDto(any(DetalleCarrito.class))).thenReturn(new DetalleCarritoOutputDto());

        mockMvc.perform(get("/detalles-carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(detalleCarritoService.findById(id)).thenReturn(new DetalleCarrito());
        when(detalleCarritoMapper.toDto(any(DetalleCarrito.class))).thenReturn(new DetalleCarritoOutputDto());

        mockMvc.perform(get("/detalles-carrito/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByCarritoId() throws Exception {
        Long id = 1L;
        Page<DetalleCarrito> page = new PageImpl<>(List.of(new DetalleCarrito()));
        when(detalleCarritoService.findByCarritoId(eq(id), any(Pageable.class))).thenReturn(page);
        when(detalleCarritoMapper.toDto(any(DetalleCarrito.class))).thenReturn(new DetalleCarritoOutputDto());

        mockMvc.perform(get("/detalles-carrito/carrito/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        DetalleCarritoInputDto inputDto = new DetalleCarritoInputDto();
        DetalleCarrito detalle = new DetalleCarrito();
        DetalleCarritoOutputDto outputDto = new DetalleCarritoOutputDto();

        when(detalleCarritoMapper.toEntity(any(DetalleCarritoInputDto.class))).thenReturn(detalle);
        when(detalleCarritoService.update(eq(id), any(DetalleCarrito.class))).thenReturn(detalle);
        when(detalleCarritoMapper.toDto(any(DetalleCarrito.class))).thenReturn(outputDto);

        mockMvc.perform(put("/detalles-carrito/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/detalles-carrito/{id}", id))
                .andExpect(status().isNoContent());
    }
}
