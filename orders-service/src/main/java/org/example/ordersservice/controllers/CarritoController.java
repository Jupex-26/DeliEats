package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.carrito.CarritoInputDto;
import org.example.ordersservice.dtos.carrito.CarritoOutputDto;
import org.example.ordersservice.mappers.CarritoMapper;
import org.example.ordersservice.models.Carrito;
import org.example.ordersservice.services.CarritoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoMapper carritoMapper;

    @PostMapping
    public ResponseEntity<CarritoOutputDto> create(@Valid @RequestBody CarritoInputDto dto) {
        Carrito entity = carritoMapper.toEntity(dto);
        Carrito saved = carritoService.create(entity);
        return new ResponseEntity<>(carritoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CarritoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<CarritoOutputDto> dtos = carritoService.findAll(pageable)
                .map(carritoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoOutputDto> findById(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return ResponseEntity.ok(carritoMapper.toDto(carrito));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoOutputDto> findByClienteId(@PathVariable Long usuarioId) {
        Carrito carrito = carritoService.findByClienteId(usuarioId);
        return ResponseEntity.ok(carritoMapper.toDto(carrito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        carritoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/limpiar")
    public ResponseEntity<Void> clearCarrito(@PathVariable Long id) {
        carritoService.clearCarrito(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/productos/{productoId}")
    public ResponseEntity<CarritoOutputDto> updateCantidad(
            @PathVariable Long id,
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {

        Carrito actualizado = carritoService.addProducto(id, productoId, cantidad);
        return ResponseEntity.ok(carritoMapper.toDto(actualizado));
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> calculateTotal(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.calculateTotal(id));
    }

    @DeleteMapping("/{id}/productos/{productoId}")
    public ResponseEntity<CarritoOutputDto> removeProducto(
            @PathVariable Long id,
            @PathVariable Long productoId) {

        Carrito carrito = carritoService.removeProducto(id, productoId);
        return ResponseEntity.ok(carritoMapper.toDto(carrito));
    }
}
