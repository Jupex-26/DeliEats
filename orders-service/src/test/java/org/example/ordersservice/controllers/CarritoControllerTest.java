package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.carrito.CarritoInputDto;
import org.example.ordersservice.dtos.carrito.CarritoOutputDto;
import org.example.ordersservice.mappers.CarritoMapper;
import org.example.ordersservice.models.Carrito;
import org.example.ordersservice.services.CarritoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    @MockBean
    private CarritoMapper carritoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        CarritoInputDto inputDto = new CarritoInputDto();
        Carrito carrito = new Carrito();
        CarritoOutputDto outputDto = new CarritoOutputDto();

        when(carritoMapper.toEntity(any(CarritoInputDto.class))).thenReturn(carrito);
        when(carritoService.create(any(Carrito.class))).thenReturn(carrito);
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(outputDto);

        mockMvc.perform(post("/carritos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Carrito> page = new PageImpl<>(List.of(new Carrito()));
        when(carritoService.findAll(any(Pageable.class))).thenReturn(page);
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(new CarritoOutputDto());

        mockMvc.perform(get("/carritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(carritoService.findById(id)).thenReturn(new Carrito());
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(new CarritoOutputDto());

        mockMvc.perform(get("/carritos/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByClienteId() throws Exception {
        Long id = 1L;
        when(carritoService.findByClienteId(id)).thenReturn(new Carrito());
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(new CarritoOutputDto());

        mockMvc.perform(get("/carritos/usuario/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/carritos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void clearCarrito() throws Exception {
        Long id = 1L;
        mockMvc.perform(post("/carritos/{id}/limpiar", id))
                .andExpect(status().isOk());
    }

    @Test
    void updateCantidad() throws Exception {
        Long id = 1L;
        Long productoId = 10L;
        int cantidad = 5;
        when(carritoService.addProducto(id, productoId, cantidad)).thenReturn(new Carrito());
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(new CarritoOutputDto());

        mockMvc.perform(put("/carritos/{id}/productos/{productoId}", id, productoId)
                .param("cantidad", String.valueOf(cantidad)))
                .andExpect(status().isOk());
    }

    @Test
    void calculateTotal() throws Exception {
        Long id = 1L;
        when(carritoService.calculateTotal(id)).thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(get("/carritos/{id}/total", id))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    void removeProducto() throws Exception {
        Long id = 1L;
        Long productoId = 10L;
        when(carritoService.removeProducto(id, productoId)).thenReturn(new Carrito());
        when(carritoMapper.toDto(any(Carrito.class))).thenReturn(new CarritoOutputDto());

        mockMvc.perform(delete("/carritos/{id}/productos/{productoId}", id, productoId))
                .andExpect(status().isOk());
    }
}
