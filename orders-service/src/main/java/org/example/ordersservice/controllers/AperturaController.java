package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.apertura.AperturaInputDto;
import org.example.ordersservice.dtos.apertura.AperturaOutputDto;
import org.example.ordersservice.mappers.AperturaMapper;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.services.AperturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<AperturaOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<AperturaOutputDto> dtos = aperturaService.findAll(pageable)
                .map(aperturaMapper::toDto);
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
