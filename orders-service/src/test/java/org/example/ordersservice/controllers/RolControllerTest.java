package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.rol.RolInputDto;
import org.example.ordersservice.dtos.rol.RolOutputDto;
import org.example.ordersservice.mappers.RolMapper;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.services.RolService;
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

@WebMvcTest(RolController.class)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolMapper rolMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        RolInputDto inputDto = new RolInputDto();
        Rol rol = new Rol();
        RolOutputDto outputDto = new RolOutputDto();

        when(rolMapper.toEntity(any(RolInputDto.class))).thenReturn(rol);
        when(rolService.save(any(Rol.class))).thenReturn(rol);
        when(rolMapper.toDto(any(Rol.class))).thenReturn(outputDto);

        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Rol> page = new PageImpl<>(List.of(new Rol()));
        when(rolService.findAll(any(Pageable.class))).thenReturn(page);
        when(rolMapper.toDto(any(Rol.class))).thenReturn(new RolOutputDto());

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(rolService.findById(id)).thenReturn(new Rol());
        when(rolMapper.toDto(any(Rol.class))).thenReturn(new RolOutputDto());

        mockMvc.perform(get("/roles/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        RolInputDto inputDto = new RolInputDto();
        Rol rol = new Rol();
        RolOutputDto outputDto = new RolOutputDto();

        when(rolMapper.toEntity(any(RolInputDto.class))).thenReturn(rol);
        when(rolService.update(eq(id), any(Rol.class))).thenReturn(rol);
        when(rolMapper.toDto(any(Rol.class))).thenReturn(outputDto);

        mockMvc.perform(put("/roles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/roles/{id}", id))
                .andExpect(status().isNoContent());
    }
}
