package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.carrito.CarritoInputDto;
import org.example.ordersservice.dtos.carrito.CarritoOutputDto;
import org.example.ordersservice.mappers.CarritoMapper;
import org.example.ordersservice.models.Carrito;
import org.example.ordersservice.services.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoMapper carritoMapper;

    @PostMapping
    public ResponseEntity<CarritoOutputDto> create(@RequestBody CarritoInputDto dto) {
        Carrito entity = carritoMapper.toEntity(dto);
        Carrito saved = carritoService.create(entity);
        return new ResponseEntity<>(carritoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CarritoOutputDto>> findAll() {
        List<CarritoOutputDto> dtos = carritoService.findAll()
                .stream()
                .map(carritoMapper::toDto)
                .collect(Collectors.toList());
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
}
