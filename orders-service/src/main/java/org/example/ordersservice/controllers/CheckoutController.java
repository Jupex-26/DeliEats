package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.User;
import org.example.ordersservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<PedidoOutputDto> finalizarCompra(@AuthenticationPrincipal User usuario) {
        PedidoOutputDto response = orderService.procesarCheckout(usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
