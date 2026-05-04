package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.empresa.EmpresaInputDto;
import org.example.ordersservice.dtos.empresa.EmpresaOutputDto;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.mappers.EmpresaMapper;
import org.example.ordersservice.services.EmpresaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaMapper empresaMapper;

    @PostMapping
    public ResponseEntity<EmpresaOutputDto> create(@RequestBody EmpresaInputDto dto) {
        Empresa entity = empresaMapper.toEntity(dto);
        Empresa saved = empresaService.save(entity);
        return new ResponseEntity<>(empresaMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EmpresaOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<EmpresaOutputDto> dtos = empresaService.findAll(pageable)
                .map(empresaMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaOutputDto> findById(@PathVariable Long id) {
        Empresa entity = empresaService.findById(id);
        return ResponseEntity.ok(empresaMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaOutputDto> update(@PathVariable Long id, @RequestBody EmpresaInputDto dto) {
        Empresa entity = empresaMapper.toEntity(dto);
        Empresa updated = empresaService.update(id, entity);
        return ResponseEntity.ok(empresaMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
