package org.example.ordersservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.exception.custom.QuantityExceedsException;
import org.example.ordersservice.exception.custom.UnauthorizedException;
import org.example.ordersservice.models.Carrito;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.repositories.CarritoRepository;
import org.example.ordersservice.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    private final DetalleCarritoService detalleCarritoService;
    private final ClienteService clienteService;
    private final EmpresaService empresaService;

    @Override
    public Page<Carrito> findAll(Pageable pageable) {
        return carritoRepository.findAll(pageable);
    }

    @Override
    public Carrito findById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado con ID: " + id));
    }

    @Override
    public Carrito findByClienteId(Long usuarioId) {
        return carritoRepository.findByClienteId(usuarioId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado con ID de usuario: " + usuarioId));
    }

    @Override
    public Carrito create(Carrito carrito) {
        Cliente cliente = clienteService.findById(carrito.getCliente().getId());

        Empresa empresa = empresaService.findById(carrito.getEmpresaId());

        if (empresa.isNotOpen()){
            throw new UnauthorizedException("No puede agregar un producto de una empresa cerrada");
        }
        carrito.setCliente(cliente);

        carrito.getDetalles().forEach(d -> d.setCarrito(carrito));

        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteById(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public Carrito addProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = findById(carritoId);
        Producto producto = productoService.findById(productoId);

        if (cantidad<=0 || producto.getCantidad() < cantidad){
            throw new QuantityExceedsException("La cantidad debe ser mayor a 0 y no debe exceder el stock disponible (" + producto.getCantidad() + ")");
        }
        carrito.agregarProducto(producto,cantidad);

        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito removeProducto(Long carritoId, Long productoId) {
        Carrito carrito = findById(carritoId);

        carrito.eliminarProducto(productoId);

        return carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void clearCarrito(Long carritoId) {
        Carrito carrito = findById(carritoId);
        detalleCarritoService.deleteByCarritoId(carrito.getId());
        carrito.cleanDetalles();
    }

    @Override
    public BigDecimal calculateTotal(Long carritoId) {
        Carrito carrito = findById(carritoId);

        return carrito.getPrecioTotal();
    }

}
