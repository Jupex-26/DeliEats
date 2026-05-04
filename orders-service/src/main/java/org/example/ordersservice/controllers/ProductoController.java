package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.producto.ProductoInputDto;
import org.example.ordersservice.dtos.producto.ProductoOutputDto;
import org.example.ordersservice.models.Producto;
import org.example.ordersservice.mappers.ProductoMapper;
import org.example.ordersservice.services.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    @PostMapping
    public ResponseEntity<ProductoOutputDto> create(@Valid @RequestBody ProductoInputDto dto) {
        Producto entity = productoMapper.toEntity(dto);
        Producto saved = productoService.save(entity);
        return new ResponseEntity<>(productoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<ProductoOutputDto> dtos = productoService.findAll(pageable)
                .map(productoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoOutputDto> findById(@PathVariable Long id) {
        Producto entity = productoService.findById(id);
        return ResponseEntity.ok(productoMapper.toDto(entity));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<ProductoOutputDto>> findByCategoriaId(@PathVariable Long categoriaId, @PageableDefault Pageable pageable) {
        Page<ProductoOutputDto> dtos = productoService.findByCategoriaId(categoriaId, pageable)
                .map(productoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoOutputDto> update(@PathVariable Long id, @Valid @RequestBody ProductoInputDto dto) {
        Producto entity = productoMapper.toEntity(dto);
        Producto updated = productoService.update(id, entity);
        return ResponseEntity.ok(productoMapper.toDto(updated));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        productoService.updateStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
