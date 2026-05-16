package org.example.ordersservice.services.impl;

import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.exception.custom.CarritoVacioException;
import org.example.ordersservice.exception.custom.QuantityExceedsException;
import org.example.ordersservice.mappers.PedidoMapper;
import org.example.ordersservice.models.*;
import org.example.ordersservice.services.CarritoService;
import org.example.ordersservice.services.EstadoService;
import org.example.ordersservice.services.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CarritoService carritoService;
    @Mock
    private PedidoService pedidoService;
    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private EstadoService estadoService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Carrito carrito;
    private Cliente cliente; // Changed from User to Cliente
    private Empresa empresa;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(); // Instantiate Cliente directly
        cliente.setId(1L);

        empresa = new Empresa();
        empresa.setId(10L);

        producto = new Producto();
        producto.setId(100L);
        producto.setCantidad(10);
        producto.setPrecio(new BigDecimal("10.00"));
        producto.setEmpresa(empresa);

        DetalleCarrito detalle = new DetalleCarrito();
        detalle.setProducto(producto);
        detalle.setCantidad(2);

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCliente(cliente); // Assign the Cliente instance
        carrito.setDetalles(new ArrayList<>(List.of(detalle)));
    }

    @Test
    void procesarCheckout_Success() {
        Long userId = 1L;
        Estado estadoPendiente = new Estado();
        Pedido pedidoGuardado = new Pedido();
        PedidoOutputDto pedidoOutputDto = new PedidoOutputDto();

        when(carritoService.findByClienteId(userId)).thenReturn(carrito);
        when(estadoService.findByNombre("PENDIENTE")).thenReturn(estadoPendiente);
        when(pedidoService.save(any(Pedido.class))).thenReturn(pedidoGuardado);
        when(pedidoMapper.toDto(pedidoGuardado)).thenReturn(pedidoOutputDto);

        PedidoOutputDto result = orderService.procesarCheckout(userId);

        assertNotNull(result);
        assertEquals(8, producto.getCantidad()); // Check stock reduction
        verify(carritoService).findByClienteId(userId);
        verify(estadoService).findByNombre("PENDIENTE");
        verify(pedidoService).save(any(Pedido.class));
        verify(carritoService).clearCarrito(carrito.getId());
        verify(pedidoMapper).toDto(pedidoGuardado);
    }

    @Test
    void procesarCheckout_CarritoVacio() {
        Long userId = 1L;
        carrito.setDetalles(new ArrayList<>());
        when(carritoService.findByClienteId(userId)).thenReturn(carrito);

        assertThrows(CarritoVacioException.class, () -> orderService.procesarCheckout(userId));
        verify(carritoService).findByClienteId(userId);
        verify(pedidoService, never()).save(any(Pedido.class));
    }

    @Test
    void procesarCheckout_StockInsuficiente() {
        Long userId = 1L;
        producto.setCantidad(1); // Less than required
        when(carritoService.findByClienteId(userId)).thenReturn(carrito);

        assertThrows(QuantityExceedsException.class, () -> orderService.procesarCheckout(userId));
        verify(carritoService).findByClienteId(userId);
        verify(pedidoService, never()).save(any(Pedido.class));
    }
}
