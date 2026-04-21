package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.apertura.AperturaInputDto;
import org.example.ordersservice.dtos.apertura.AperturaOutputDto;
import org.example.ordersservice.mappers.AperturaMapper;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.services.AperturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aperturas")
@RequiredArgsConstructor
public class AperturaController {

    private final AperturaService aperturaService;
    private final AperturaMapper aperturaMapper;

    @PostMapping
    public ResponseEntity<AperturaOutputDto> create(@RequestBody AperturaInputDto dto) {
        Apertura entity = aperturaMapper.toEntity(dto);
        Apertura saved = aperturaService.save(entity);
        return new ResponseEntity<>(aperturaMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AperturaOutputDto>> findAll() {
        List<AperturaOutputDto> dtos = aperturaService.findAll()
                .stream()
                .map(aperturaMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AperturaOutputDto> findById(@PathVariable Long id) {
        Apertura apertura = aperturaService.findById(id);
        return ResponseEntity.ok(aperturaMapper.toDto(apertura));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AperturaOutputDto> update(@PathVariable Long id, @RequestBody AperturaInputDto dto) {
        Apertura updated = aperturaService.update(id, aperturaMapper.toEntity(dto));
        return ResponseEntity.ok(aperturaMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        aperturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
