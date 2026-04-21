package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.estado.EstadoInputDto;
import org.example.ordersservice.dtos.estado.EstadoOutputDto;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.mappers.EstadoMapper;
import org.example.ordersservice.services.EstadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;

    @PostMapping
    public ResponseEntity<EstadoOutputDto> create(@RequestBody EstadoInputDto dto) {
        Estado entity = estadoMapper.toEntity(dto);
        Estado saved = estadoService.save(entity);
        return new ResponseEntity<>(estadoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EstadoOutputDto>> findAll() {
        List<EstadoOutputDto> dtos = estadoService.findAll()
                .stream()
                .map(estadoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoOutputDto> findById(@PathVariable Long id) {
        Estado entity = estadoService.findById(id);
        return ResponseEntity.ok(estadoMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoOutputDto> update(@PathVariable Long id, @RequestBody EstadoInputDto dto) {
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