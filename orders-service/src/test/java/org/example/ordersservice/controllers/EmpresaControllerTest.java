package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.empresa.EmpresaInputDto;
import org.example.ordersservice.dtos.empresa.EmpresaOutputDto;
import org.example.ordersservice.mappers.EmpresaMapper;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.services.EmpresaService;
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

@WebMvcTest(EmpresaController.class)
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    @MockBean
    private EmpresaMapper empresaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        EmpresaInputDto inputDto = new EmpresaInputDto();
        Empresa empresa = new Empresa();
        EmpresaOutputDto outputDto = new EmpresaOutputDto();

        when(empresaMapper.toEntity(any(EmpresaInputDto.class))).thenReturn(empresa);
        when(empresaService.save(any(Empresa.class))).thenReturn(empresa);
        when(empresaMapper.toDto(any(Empresa.class))).thenReturn(outputDto);

        mockMvc.perform(post("/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Empresa> page = new PageImpl<>(List.of(new Empresa()));
        when(empresaService.findAll(any(Pageable.class))).thenReturn(page);
        when(empresaMapper.toDto(any(Empresa.class))).thenReturn(new EmpresaOutputDto());

        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(empresaService.findById(id)).thenReturn(new Empresa());
        when(empresaMapper.toDto(any(Empresa.class))).thenReturn(new EmpresaOutputDto());

        mockMvc.perform(get("/empresas/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        EmpresaInputDto inputDto = new EmpresaInputDto();
        Empresa empresa = new Empresa();
        EmpresaOutputDto outputDto = new EmpresaOutputDto();

        when(empresaMapper.toEntity(any(EmpresaInputDto.class))).thenReturn(empresa);
        when(empresaService.update(eq(id), any(Empresa.class))).thenReturn(empresa);
        when(empresaMapper.toDto(any(Empresa.class))).thenReturn(outputDto);

        mockMvc.perform(put("/empresas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/empresas/{id}", id))
                .andExpect(status().isNoContent());
    }
}
