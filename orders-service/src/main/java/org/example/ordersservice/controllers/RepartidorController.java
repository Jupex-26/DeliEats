package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.repartidor.RepartidorInputDto;
import org.example.ordersservice.dtos.repartidor.RepartidorOutputDto;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.mappers.RepartidorMapper;
import org.example.ordersservice.services.RepartidorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repartidores")
@RequiredArgsConstructor
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final RepartidorMapper repartidorMapper;

    @PostMapping
    public ResponseEntity<RepartidorOutputDto> create(@Valid @RequestBody RepartidorInputDto dto) {
        Repartidor entity = repartidorMapper.toEntity(dto);
        Repartidor saved = repartidorService.save(entity);
        return new ResponseEntity<>(repartidorMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RepartidorOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<RepartidorOutputDto> dtos = repartidorService.findAll(pageable)
                .map(repartidorMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepartidorOutputDto> findById(@PathVariable Long id) {
        Repartidor entity = repartidorService.findById(id);
        return ResponseEntity.ok(repartidorMapper.toDto(entity));
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<RepartidorOutputDto> findByClienteId(@PathVariable Long clienteId) {
        Repartidor entity = repartidorService.findByClienteId(clienteId);
        return ResponseEntity.ok(repartidorMapper.toDto(entity));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<Page<RepartidorOutputDto>> findByDisponible(@PageableDefault Pageable pageable) {
        Page<RepartidorOutputDto> dtos = repartidorService.findByDisponible(true, pageable)
                .map(repartidorMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/aprobado")
    public ResponseEntity<Page<RepartidorOutputDto>> findByAprobado(
            @RequestParam boolean aprobado, 
            @PageableDefault Pageable pageable) {
        Page<RepartidorOutputDto> dtos = repartidorService.findByAprobado(aprobado, pageable)
                .map(repartidorMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepartidorOutputDto> update(@PathVariable Long id, @Valid @RequestBody RepartidorInputDto dto) {
        Repartidor entity = repartidorMapper.toEntity(dto);
        Repartidor updated = repartidorService.update(id, entity);
        return ResponseEntity.ok(repartidorMapper.toDto(updated));
    }

    @PatchMapping("/cliente/{clienteId}/disponibilidad")
    public ResponseEntity<RepartidorOutputDto> updateDisponibilidad(@PathVariable Long clienteId, @RequestParam boolean disponible) {
        Repartidor updated = repartidorService.updateDisponibilidad(clienteId, disponible);
        return ResponseEntity.ok(repartidorMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        repartidorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
