package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoInputDto;
import org.example.ordersservice.dtos.detallecarrito.DetalleCarritoOutputDto;
import org.example.ordersservice.mappers.DetalleCarritoMapper;
import org.example.ordersservice.models.DetalleCarrito;
import org.example.ordersservice.services.DetalleCarritoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalles-carrito")
@RequiredArgsConstructor
public class DetalleCarritoController {

    private final DetalleCarritoService detalleCarritoService;
    private final DetalleCarritoMapper detalleCarritoMapper;

    @PostMapping
    public ResponseEntity<DetalleCarritoOutputDto> create(@RequestBody DetalleCarritoInputDto dto) {
        DetalleCarrito entity = detalleCarritoMapper.toEntity(dto);
        DetalleCarrito saved = detalleCarritoService.save(entity);
        return new ResponseEntity<>(detalleCarritoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<DetalleCarritoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<DetalleCarritoOutputDto> dtos = detalleCarritoService.findAll(pageable)
                .map(detalleCarritoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCarritoOutputDto> findById(@PathVariable Long id) {
        DetalleCarrito entity = detalleCarritoService.findById(id);
        return ResponseEntity.ok(detalleCarritoMapper.toDto(entity));
    }

    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<Page<DetalleCarritoOutputDto>> findByCarritoId(@PathVariable Long carritoId, @PageableDefault Pageable pageable) {
        Page<DetalleCarritoOutputDto> dtos = detalleCarritoService.findByCarritoId(carritoId, pageable)
                .map(detalleCarritoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCarritoOutputDto> update(@PathVariable Long id, @RequestBody DetalleCarritoInputDto dto) {
        DetalleCarrito entity = detalleCarritoMapper.toEntity(dto);
        DetalleCarrito updated = detalleCarritoService.update(id, entity);
        return ResponseEntity.ok(detalleCarritoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        detalleCarritoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
