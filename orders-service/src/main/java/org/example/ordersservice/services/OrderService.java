package org.example.ordersservice.services;

import org.example.ordersservice.dtos.pedido.PedidoOutputDto;

public interface OrderService {
    PedidoOutputDto procesarCheckout(Long userId);
}
