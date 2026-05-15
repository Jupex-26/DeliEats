package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.tipococina.TipoCocinaInputDto;
import org.example.ordersservice.dtos.tipococina.TipoCocinaOutputDto;
import org.example.ordersservice.mappers.TipoCocinaMapper;
import org.example.ordersservice.models.TipoCocina;
import org.example.ordersservice.services.TipoCocinaService;
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

@WebMvcTest(TipoCocinaController.class)
class TipoCocinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoCocinaService tipoCocinaService;

    @MockBean
    private TipoCocinaMapper tipoCocinaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        TipoCocinaInputDto inputDto = new TipoCocinaInputDto();
        TipoCocina tipoCocina = new TipoCocina();
        TipoCocinaOutputDto outputDto = new TipoCocinaOutputDto();

        when(tipoCocinaMapper.toEntity(any(TipoCocinaInputDto.class))).thenReturn(tipoCocina);
        when(tipoCocinaService.save(any(TipoCocina.class))).thenReturn(tipoCocina);
        when(tipoCocinaMapper.toDto(any(TipoCocina.class))).thenReturn(outputDto);

        mockMvc.perform(post("/tipos-cocina")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<TipoCocina> page = new PageImpl<>(List.of(new TipoCocina()));
        when(tipoCocinaService.findAll(any(Pageable.class))).thenReturn(page);
        when(tipoCocinaMapper.toDto(any(TipoCocina.class))).thenReturn(new TipoCocinaOutputDto());

        mockMvc.perform(get("/tipos-cocina"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(tipoCocinaService.findById(id)).thenReturn(new TipoCocina());
        when(tipoCocinaMapper.toDto(any(TipoCocina.class))).thenReturn(new TipoCocinaOutputDto());

        mockMvc.perform(get("/tipos-cocina/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        TipoCocinaInputDto inputDto = new TipoCocinaInputDto();
        TipoCocina tipoCocina = new TipoCocina();
        TipoCocinaOutputDto outputDto = new TipoCocinaOutputDto();

        when(tipoCocinaMapper.toEntity(any(TipoCocinaInputDto.class))).thenReturn(tipoCocina);
        when(tipoCocinaService.update(eq(id), any(TipoCocina.class))).thenReturn(tipoCocina);
        when(tipoCocinaMapper.toDto(any(TipoCocina.class))).thenReturn(outputDto);

        mockMvc.perform(put("/tipos-cocina/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/tipos-cocina/{id}", id))
                .andExpect(status().isNoContent());
    }
}
