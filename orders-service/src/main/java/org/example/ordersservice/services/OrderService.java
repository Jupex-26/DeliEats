package org.example.ordersservice.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.exception.custom.CarritoVacioException;
import org.example.ordersservice.exception.custom.QuantityExceedsException;
import org.example.ordersservice.mappers.PedidoMapper;
import org.example.ordersservice.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final EstadoService estadoService;

    @Transactional
    public PedidoOutputDto procesarCheckout(Long userId) {
        // 1. Obtener y validar estado inicial
        Carrito carrito = carritoService.findByClienteId(userId);
        validarCarrito(carrito);

        // 2. Validar productos y descontar stock
        validarYActualizarStock(carrito);

        // 3. Obtener la empresa del primer producto del carrito
        Empresa empresa = carrito.getDetalles().getFirst().getProducto().getEmpresa();

        // 4. Crear y persistir el pedido
        Pedido pedidoGuardado = crearYGuardarPedido(carrito, empresa);

        // 5. Limpiar carrito
        carritoService.clearCarrito(carrito.getId());

        return pedidoMapper.toDto(pedidoGuardado);
    }

    private void validarCarrito(Carrito carrito) {
        if (carrito.hasNotDetalles()){
            throw new CarritoVacioException("No puedes finalizar la compra porque tu carrito está vacío.");
        }
    }

    private void validarYActualizarStock(Carrito carrito) {
        for (DetalleCarrito item : carrito.getDetalles()) {
            Producto producto = item.getProducto();

            if (Objects.isNull(producto)) {
                throw new EntityNotFoundException("Producto no encontrado en el catálogo.");
            }

            if (producto.getCantidad() < item.getCantidad()) {
                throw new QuantityExceedsException("Stock insuficiente para: " + producto.getNombre() +
                        " (Disponible: " + producto.getCantidad() + ")");
            }

            producto.setCantidad(producto.getCantidad() - item.getCantidad());
        }
    }

    private Pedido crearYGuardarPedido(Carrito carrito, Empresa empresa) {
        Estado estadoPendiente = estadoService.findByNombre("PENDIENTE");

        Pedido nuevoPedido = Pedido.builder()
                .cliente(carrito.getCliente())
                .empresa(empresa)
                .fechaCompra(LocalDateTime.now())
                .precio(carrito.getPrecioTotal())
                .estado(estadoPendiente)
                .build();

        List<DetallePedido> detalles = carrito.getDetalles().stream()
                .map(item -> DetallePedido.builder()
                        .pedido(nuevoPedido)
                        .producto(item.getProducto())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getProducto().getPrecio())
                        .build())
                .toList();

        nuevoPedido.setDetalles(detalles);
        return pedidoService.save(nuevoPedido);
    }
}
