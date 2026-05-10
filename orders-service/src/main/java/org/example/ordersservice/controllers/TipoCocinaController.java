package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.tipococina.TipoCocinaInputDto;
import org.example.ordersservice.dtos.tipococina.TipoCocinaOutputDto;
import org.example.ordersservice.mappers.TipoCocinaMapper;
import org.example.ordersservice.models.TipoCocina;
import org.example.ordersservice.services.TipoCocinaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipos-cocina")
@RequiredArgsConstructor
public class TipoCocinaController {

    private final TipoCocinaService tipoCocinaService;
    private final TipoCocinaMapper tipoCocinaMapper;

    @PostMapping
    public ResponseEntity<TipoCocinaOutputDto> create(@RequestBody TipoCocinaInputDto dto) {
        TipoCocina entity = tipoCocinaMapper.toEntity(dto);
        TipoCocina saved = tipoCocinaService.save(entity);
        return new ResponseEntity<>(tipoCocinaMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TipoCocinaOutputDto>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<TipoCocinaOutputDto> dtos = tipoCocinaService.findAll(pageable)
                .map(tipoCocinaMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCocinaOutputDto> findById(@PathVariable Long id) {
        TipoCocina entity = tipoCocinaService.findById(id);
        return ResponseEntity.ok(tipoCocinaMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCocinaOutputDto> update(@PathVariable Long id, @RequestBody TipoCocinaInputDto dto) {
        TipoCocina entity = tipoCocinaMapper.toEntity(dto);
        TipoCocina updated = tipoCocinaService.update(id, entity);
        return ResponseEntity.ok(tipoCocinaMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tipoCocinaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
