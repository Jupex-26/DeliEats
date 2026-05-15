package org.example.ordersservice.controllers;

import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.User;
import org.example.ordersservice.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    void finalizarCompra() throws Exception {
        PedidoOutputDto outputDto = new PedidoOutputDto();
        outputDto.setId(1L);

        when(orderService.procesarCheckout(anyLong())).thenReturn(outputDto);

        User mockUser = new User();
        mockUser.setId(1L);

        mockMvc.perform(post("/checkout")
                .with(user(mockUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
