package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.estado.EstadoInputDto;
import org.example.ordersservice.dtos.estado.EstadoOutputDto;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.mappers.EstadoMapper;
import org.example.ordersservice.services.EstadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;

    @PostMapping
    public ResponseEntity<EstadoOutputDto> create(@Valid @RequestBody EstadoInputDto dto) {
        Estado entity = estadoMapper.toEntity(dto);
        Estado saved = estadoService.save(entity);
        return new ResponseEntity<>(estadoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EstadoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<EstadoOutputDto> dtos = estadoService.findAll(pageable)
                .map(estadoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoOutputDto> findById(@PathVariable Long id) {
        Estado entity = estadoService.findById(id);
        return ResponseEntity.ok(estadoMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoOutputDto> update(@PathVariable Long id, @Valid @RequestBody EstadoInputDto dto) {
        Estado entity = estadoMapper.toEntity(dto);
        Estado updated = estadoService.update(id, entity);
        return ResponseEntity.ok(estadoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        estadoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
