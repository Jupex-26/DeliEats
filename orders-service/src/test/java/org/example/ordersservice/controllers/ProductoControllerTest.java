package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.producto.ProductoInputDto;
import org.example.ordersservice.dtos.producto.ProductoOutputDto;
import org.example.ordersservice.mappers.ProductoMapper;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoMapper productoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        ProductoInputDto inputDto = new ProductoInputDto();
        MockMultipartFile productoJson = new MockMultipartFile("producto", "", "application/json", objectMapper.writeValueAsBytes(inputDto));

        mockMvc.perform(multipart("/productos")
                .file(productoJson))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Producto> page = new PageImpl<>(List.of(new Producto()));
        when(productoService.findAll(any(Pageable.class))).thenReturn(page);
        when(productoMapper.toDto(any(Producto.class))).thenReturn(new ProductoOutputDto());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(productoService.findById(id)).thenReturn(new Producto());
        when(productoMapper.toDto(any(Producto.class))).thenReturn(new ProductoOutputDto());

        mockMvc.perform(get("/productos/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByCategoriaId() throws Exception {
        Long id = 1L;
        Page<Producto> page = new PageImpl<>(List.of(new Producto()));
        when(productoService.findByCategoriaId(eq(id), any(Pageable.class))).thenReturn(page);
        when(productoMapper.toDto(any(Producto.class))).thenReturn(new ProductoOutputDto());

        mockMvc.perform(get("/productos/categoria/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByEmpresaId() throws Exception {
        Long id = 1L;
        Page<Producto> page = new PageImpl<>(List.of(new Producto()));
        when(productoService.findByEmpresaId(eq(id), any(Pageable.class))).thenReturn(page);
        when(productoMapper.toDto(any(Producto.class))).thenReturn(new ProductoOutputDto());

        mockMvc.perform(get("/productos/empresa/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        ProductoInputDto inputDto = new ProductoInputDto();
        MockMultipartFile productoJson = new MockMultipartFile("producto", "", "application/json", objectMapper.writeValueAsBytes(inputDto));

        mockMvc.perform(multipart(HttpMethod.PUT, "/productos/{id}", id)
                .file(productoJson))
                .andExpect(status().isOk());
    }

    @Test
    void updateStock() throws Exception {
        Long id = 1L;
        int cantidad = 10;
        mockMvc.perform(patch("/productos/{id}/stock", id)
                .param("cantidad", String.valueOf(cantidad)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/productos/{id}", id))
                .andExpect(status().isNoContent());
    }
}
