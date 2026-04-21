package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.mappers.RepartidorMapper;
import org.example.ordersservice.services.RepartidorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repartidores")
@RequiredArgsConstructor
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final RepartidorMapper repartidorMapper;

    @PostMapping
    public ResponseEntity<RepartidorOutputDto> create(@RequestBody RepartidorInputDto dto) {
        Repartidor entity = repartidorMapper.toEntity(dto);
        Repartidor saved = repartidorService.save(entity);
        return new ResponseEntity<>(repartidorMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RepartidorOutputDto>> findAll() {
        List<RepartidorOutputDto> dtos = repartidorService.findAll()
                .stream()
                .map(repartidorMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepartidorOutputDto> findById(@PathVariable Long id) {
        Repartidor entity = repartidorService.findById(id);
        return ResponseEntity.ok(repartidorMapper.toDto(entity));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<RepartidorOutputDto>> findByDisponible() {
        List<RepartidorOutputDto> dtos = repartidorService.findByDisponible(true)
                .stream()
                .map(repartidorMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepartidorOutputDto> update(@PathVariable Long id, @RequestBody RepartidorInputDto dto) {
        Repartidor entity = repartidorMapper.toEntity(dto);
        Repartidor updated = repartidorService.update(id, entity);
        return ResponseEntity.ok(repartidorMapper.toDto(updated));
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<RepartidorOutputDto> updateDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        Repartidor updated = repartidorService.updateDisponibilidad(id, disponible);
        return ResponseEntity.ok(repartidorMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        repartidorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
