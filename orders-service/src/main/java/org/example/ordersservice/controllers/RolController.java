package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.rol.RolInputDto;
import org.example.ordersservice.dtos.rol.RolOutputDto;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.mappers.RolMapper;
import org.example.ordersservice.services.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;
    private final RolMapper rolMapper;

    @PostMapping
    public ResponseEntity<RolOutputDto> create(@RequestBody RolInputDto dto) {
        Rol entity = rolMapper.toEntity(dto);
        Rol saved = rolService.save(entity);
        return new ResponseEntity<>(rolMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RolOutputDto>> findAll() {
        List<RolOutputDto> dtos = rolService.findAll()
                .stream()
                .map(rolMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolOutputDto> findById(@PathVariable Long id) {
        Rol entity = rolService.findById(id);
        return ResponseEntity.ok(rolMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolOutputDto> update(@PathVariable Long id, @RequestBody RolInputDto dto) {
        Rol entity = rolMapper.toEntity(dto);
        Rol updated = rolService.update(id, entity);
        return ResponseEntity.ok(rolMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}