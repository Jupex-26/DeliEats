package org.example.ordersservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ordersservice.dtos.pedido.PedidoInputDto;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.mappers.PedidoMapper;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.services.PedidoService;
import org.example.ordersservice.services.PdfService;
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

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private PedidoMapper pedidoMapper;

    @MockBean
    private PdfService pdfService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        PedidoInputDto inputDto = new PedidoInputDto();
        Pedido pedido = new Pedido();
        PedidoOutputDto outputDto = new PedidoOutputDto();

        when(pedidoMapper.toEntity(any(PedidoInputDto.class))).thenReturn(pedido);
        when(pedidoService.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(outputDto);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void findAll() throws Exception {
        Page<Pedido> page = new PageImpl<>(List.of(new Pedido()));
        when(pedidoService.findAll(any(Pageable.class))).thenReturn(page);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findById() throws Exception {
        Long id = 1L;
        when(pedidoService.findById(id)).thenReturn(new Pedido());
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void findByClienteId() throws Exception {
        Long id = 1L;
        Page<Pedido> page = new PageImpl<>(List.of(new Pedido()));
        when(pedidoService.findByClienteId(eq(id), any(Pageable.class))).thenReturn(page);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos/cliente/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByEmpresaId() throws Exception {
        Long id = 1L;
        Page<Pedido> page = new PageImpl<>(List.of(new Pedido()));
        when(pedidoService.findByEmpresaId(eq(id), any(Pageable.class))).thenReturn(page);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos/empresa/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByEmpresaIdMesActual() throws Exception {
        Long id = 1L;
        Page<Pedido> page = new PageImpl<>(List.of(new Pedido()));
        when(pedidoService.findByEmpresaIdMesActual(eq(id), any(Pageable.class))).thenReturn(page);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos/empresa/{id}/mes-actual", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void findByEmpresaIdAndMes() throws Exception {
        Long id = 1L;
        int mes = 5;
        int anio = 2026;
        Page<Pedido> page = new PageImpl<>(List.of(new Pedido()));
        when(pedidoService.findByEmpresaIdAndMesAndAnio(eq(id), eq(mes), eq(anio), any(Pageable.class))).thenReturn(page);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(get("/pedidos/empresa/{id}/mes", id)
                .param("mes", String.valueOf(mes))
                .param("anio", String.valueOf(anio)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void update() throws Exception {
        Long id = 1L;
        PedidoInputDto inputDto = new PedidoInputDto();
        Pedido pedido = new Pedido();
        PedidoOutputDto outputDto = new PedidoOutputDto();

        when(pedidoMapper.toEntity(any(PedidoInputDto.class))).thenReturn(pedido);
        when(pedidoService.update(eq(id), any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(outputDto);

        mockMvc.perform(put("/pedidos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateEstado() throws Exception {
        Long id = 1L;
        Long estadoId = 2L;
        when(pedidoService.updateEstado(id, estadoId)).thenReturn(new Pedido());
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(patch("/pedidos/{id}/estado/{estadoId}", id, estadoId))
                .andExpect(status().isOk());
    }

    @Test
    void cancelarPedido() throws Exception {
        Long id = 1L;
        when(pedidoService.cancelarPedido(id)).thenReturn(new Pedido());
        when(pedidoMapper.toDto(any(Pedido.class))).thenReturn(new PedidoOutputDto());

        mockMvc.perform(patch("/pedidos/{id}/cancelar", id))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/pedidos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void descargarFactura() throws Exception {
        Long id = 1L;
        byte[] pdfBytes = "test pdf".getBytes();
        when(pedidoService.findById(id)).thenReturn(new Pedido());
        when(pdfService.generarFactura(any(Pedido.class))).thenReturn(pdfBytes);

        mockMvc.perform(get("/pedidos/{id}/factura", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
